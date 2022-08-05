package table.traffic.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Таблица с данными по трафику
 *
 * Модель работы таблицы следующая:
 * 1. записи и взаимодействие с данными ведется на понятии интервала времени.
 * 2. у интервала времени есть начало и окончание
 * 3. не должно быть ситуации кода интервалы времени пересекаются друг с другом (согласно документации)
 * 4. уникальность записи определяется как имя пакета + информация интервала (начало + конец)
 *
 * при получении информации могут быть дубли пакетов, но с разными интервалами, для математических
 * расчетов необходимо учитывать этот факт.
 *
 * @param privateKey уникальный ключ всей записи. Считается как строка из [packageName] и [timeStart] и [timeEnd]
 * @param packageName имя пакета приложения
 * @param packageUid уникальный идентификатор пакета приложения
 * @param timeStart стартовое время интервала сбора данных
 * @param timeEnd конечное время интервала сбора данных
 * @param rxBytes количество отправленный байтов
 * @param txBytes количество полученных байтов
 * @param networkType идентификатор сети
 *
 * @author gromiloff
 * */
@Entity(tableName = TrafficEntity.TABLE_NAME)
internal data class TrafficEntity (
    @ColumnInfo(name = ID) @PrimaryKey var privateKey: String = "",

    @ColumnInfo(name = PACKAGE_NAME) var packageName: String = "",
    @ColumnInfo(name = PACKAGE_UID) var packageUid: Int = Int.MIN_VALUE,

    @ColumnInfo(name = TIME_FROM) var timeStart: Long = 0L,
    @ColumnInfo(name = TIME_TO) var timeEnd: Long = 0L,

    @ColumnInfo(name = RX_BYTES) var rxBytes: Long = 0L,
    @ColumnInfo(name = TX_BYTES) var txBytes: Long = 0L,

    @ColumnInfo(name = NETWORK) var networkType: Int = Int.MIN_VALUE,
) {
    companion object {
        fun create(
            packageName: String,
            packageUid: Int,
            asWifiNetwork: Boolean,
            rxBytes: Long,
            txBytes: Long,
            timeStart: Long,
            timeEnd: Long
        ) = TrafficEntity(
            privateKey = packageName.plus("|").plus(timeStart).plus("|").plus(timeEnd),
            packageName = packageName,
            packageUid = packageUid,
            rxBytes = rxBytes,
            txBytes = txBytes,
            networkType = if(asWifiNetwork) NETWORK_WIFI else NETWORK_OTHER,
            timeStart = timeStart,
            timeEnd = timeEnd
        )

        // модели данных провайдера трафика
        private const val NETWORK_WIFI = 1
        private const val NETWORK_OTHER = 0

        // table consts
        const val TABLE_NAME = "traffic"
        const val ID = "privateKey"
        const val PACKAGE_NAME = "packageName"
        const val PACKAGE_UID = "packageUid"
        const val TIME_FROM = "time_from"
        const val TIME_TO = "time_to"
        const val RX_BYTES = "rx_bytes"
        const val TX_BYTES = "tx_bytes"
        const val NETWORK = "networkType"
    }
}