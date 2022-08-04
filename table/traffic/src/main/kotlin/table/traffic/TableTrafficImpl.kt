package table.traffic

import api.table.TableTrafficApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import table.traffic.room.TrafficDao
import table.traffic.room.TrafficEntity

/**
 * Реализация логики управления данными с трафиком
 * */
internal data class TableTrafficImpl(
    private val trafficDao: TrafficDao
) : TableTrafficApi {
    override suspend fun insertOrReplace(packageName: String, bytes: Long, asWifiNetwork: Boolean, timeStart: Long, timeEnd: Long) {
        withContext(Dispatchers.IO) {
            trafficDao.insertOrReplace(TrafficEntity.create(packageName, bytes, asWifiNetwork, timeStart, timeEnd))
        }
    }

    override suspend fun createJsonFromDatabase(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun clear() {
        withContext(Dispatchers.IO) {
            trafficDao.deleteAll()
        }
    }
}