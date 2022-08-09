package repository.sync

import android.Manifest
import android.app.AppOpsManager
import android.app.usage.NetworkStats
import android.app.usage.NetworkStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Process
import androidx.annotation.WorkerThread
import api.function.SyncApi
import api.ll.PermissionWrap
import api.table.TableTrafficApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent
import repository.sync.packages.PackageHelper
import java.util.*


/**
 * Реализация логики управления синхронизацией данных
 * */
internal object SyncImpl : SyncApi {
    override suspend fun start(from: Long, to: Long) {
        withContext(Dispatchers.IO) {
            val storeApi = KoinJavaComponent.get<TableTrafficApi>(TableTrafficApi::class.java)
            val context = KoinJavaComponent.get<Context>(Context::class.java)
            // получаем актуальный фильтрованный список всех пакетов и их uid
            val pairs = PackageHelper(context.packageManager, context.packageName).getAllPackages()

            val lastSync = storeApi.lastUpdate()
            // время начала считаем от предыдущего либо -12 часов от текущего (тестовые данные)
            val timeFrom = if(lastSync > 0L) lastSync else to - 12 * 60 * 60 * 1000
            (context.getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager).let { manager ->
                pairs.mapTo(LinkedList()) {
                    async {
                        it.second.syncUid(manager, timeFrom, to).forEach { bucket ->
                            storeApi.insertOrReplace(
                                packageName = it.first,
                                packageUid = it.second,
                                asWifiNetwork = true,
                                rxBytes = bucket.rxBytes,
                                txBytes = bucket.txBytes,
                                timeStart = bucket.startTimeStamp,
                                timeEnd = bucket.endTimeStamp
                            )
                        }
                    }
                }.forEach { it.await() }
            }
        }
    }

    override fun requestPermissions(): HashSet<PermissionWrap> = HashSet<PermissionWrap>().apply {
        val context = KoinJavaComponent.get<Context>(Context::class.java)
        val mode = (context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager).checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), context.packageName
        )

        val granted = if (mode == AppOpsManager.MODE_DEFAULT) {
            context.checkCallingOrSelfPermission(Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED
        } else {
            mode == AppOpsManager.MODE_ALLOWED
        }

        if(!granted) this.add(PermissionWrap.Settings)
    }

    @WorkerThread
    private fun Int.syncUid(manager : NetworkStatsManager, from: Long, to: Long) : List<NetworkStats.Bucket> {
        val summary = manager.queryDetailsForUid(
            ConnectivityManager.TYPE_WIFI,
            null,
            from,
            to,
            this
        )
        return ArrayList<NetworkStats.Bucket>().apply {
            do {
                val bucket = NetworkStats.Bucket()
                summary.getNextBucket(bucket)
                this += bucket
            } while (summary.hasNextBucket())
        }
    }
}
