package api.table

import module.FeatureDataBaseApiModule

/**
 * Публичные функции для работы с таблицей данных с трафиком
 * @author gromiloff
 * */
interface TableTrafficApi : FeatureDataBaseApiModule {
    /** создать новую запись или обновить предыдущую */
    suspend fun insertOrReplace(
        packageName: String,
        bytes: Long,
        asWifiNetwork: Boolean,
        timeStart: Long,
        timeEnd: Long
    )
}