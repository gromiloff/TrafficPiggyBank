package table.traffic.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Таблица с данными по трафику
 *
 * @param privateKey уникальный ключ всей записи. Считается как строка из [packageName] и [timeStart] и [timeEnd]
 * @param packageName имя пакета приложения
 * @param timeStart стартовое время интервала сбора данных
 * @param timeEnd конечное время интервала сбора данных
 * @param bytes количество байтов трафика
 * @param networkType идентификатор сети
 *
 * @author gromiloff
 * */
@Entity(tableName = TrafficEntity.TABLE_NAME)
internal data class TrafficEntity (
    @ColumnInfo(name = ID) @PrimaryKey var privateKey: String = "",
    @ColumnInfo(name = PACKAGE) var packageName: String = "",
    @ColumnInfo(name = TIME_FROM) var timeStart: Long = 0L,
    @ColumnInfo(name = TIME_TO) var timeEnd: Long = 0L,
    @ColumnInfo(name = BYTES) var bytes: Long = 0L,
    @ColumnInfo(name = NETWORK) var networkType: Int = Int.MIN_VALUE,
) {
    companion object {
        fun create(
            packageName: String,
            bytes: Long,
            asWifiNetwork: Boolean,
            timeStart: Long,
            timeEnd: Long
        ) = TrafficEntity(
                privateKey = packageName.plus("|").plus(timeStart).plus("|").plus(timeEnd),
                packageName = packageName,
                bytes = bytes,
                networkType = if(asWifiNetwork) NETWORK_WIFI else NETWORK_OTHER,
                timeStart = timeStart,
                timeEnd = timeEnd
            )

        const val NETWORK_WIFI = 1
        const val NETWORK_OTHER = 0

        // table consts
        const val TABLE_NAME = "traffic"
        const val ID = "privateKey"
        const val PACKAGE = "packageName"
        const val TIME_FROM = "time_from"
        const val TIME_TO = "time_to"
        const val BYTES = "bytes"
        const val NETWORK = "networkType"
    }
}