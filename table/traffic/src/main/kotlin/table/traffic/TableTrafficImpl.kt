package table.traffic

import api.table.TableTrafficApi
import api.table.TrafficInterval
import com.google.gson.GsonBuilder
import impl.printBytes
import impl.printTimeStump
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import model.TrafficModel
import table.traffic.room.TrafficDao
import table.traffic.room.TrafficEntity
import timber.log.Timber
import java.util.*

/**
 * Реализация логики управления данными с трафиком
 * */
internal data class TableTrafficImpl(
    private val trafficDao: TrafficDao
) : TableTrafficApi {
    override fun lastUpdate(): Long {
        return PrefHelper.get()
    }

    override suspend fun insertOrReplace(
        packageName: String,
        packageUid: Int,
        asWifiNetwork: Boolean,
        rxBytes: Long,
        txBytes: Long,
        timeStart: Long,
        timeEnd: Long
    ) {
        if(timeStart == 0L && timeEnd == 0L) {
            Timber.d("insertOrReplace skip >> $packageName RX=${printBytes(rxBytes)} TX=${printBytes(txBytes)} in [${printTimeStump(timeStart)}-${printTimeStump(timeEnd)}]")
            return
        }
        Timber.d("insertOrReplace >> $packageName RX=${printBytes(rxBytes)} TX=${printBytes(txBytes)} in [${printTimeStump(timeStart)}-${printTimeStump(timeEnd)}]")
        withContext(Dispatchers.IO) {
            trafficDao.insertOrReplace(TrafficEntity.create(packageName, packageUid, asWifiNetwork, rxBytes, txBytes, timeStart, timeEnd))
        }
        PrefHelper.set(timeEnd)
    }

    override suspend fun all(): List<TrafficModel> {
        val result = ArrayList<TrafficModel>()
        withContext(Dispatchers.IO) {
            val raw = trafficDao.all()
            if(raw.isEmpty()) Timber.d("empty table")
            else {
                // имя пакета + список всех записей по пакету
                val hash = HashMap<String, LinkedList<TrafficEntity>>()
                raw.forEach { entity ->
                    var list = hash[entity.packageName]
                    if(list == null) {
                        list = LinkedList<TrafficEntity>()
                        hash[entity.packageName] = list
                    }
                    list.add(entity)
                }
                hash.forEach { map ->
                    var uid : Int? = null
                    var timeMin = Long.MAX_VALUE
                    var timeMax = Long.MIN_VALUE
                    var rxBytes = 0L
                    var txBytes = 0L

                    map.value.forEach {
                        uid ?: run { uid = it.packageUid }
                        timeMin = timeMin.coerceAtMost(it.timeStart)
                        timeMax = timeMax.coerceAtLeast(it.timeEnd)
                        rxBytes+=it.rxBytes
                        txBytes+=it.txBytes
                    }
                    result.add(
                        TrafficModel(
                            packageName = map.key,
                            packageUid = uid!!,
                            timeStart = timeMin,
                            timeEnd = timeMax,
                            rxBytes = rxBytes,
                            txBytes = txBytes,
                            networkType = Int.MIN_VALUE,
                        )
                    )
                }
            }
        }
        return result
    }

    override suspend fun all(interval: TrafficInterval): List<TrafficModel> {
        TODO("Not yet implemented")
    }

    override suspend fun createJsonFromDatabase(): String {
        val gson = GsonBuilder().setPrettyPrinting().create()
        val data = trafficDao.all()
        return "== TRAFFIC DB START =\n"
            .plus(gson.toJson(data))
            .plus("== TRAFFIC DB END ==\n")
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            trafficDao.deleteAll()
        }
    }
}