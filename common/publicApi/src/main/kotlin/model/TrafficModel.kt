package model

/**
 * Класс для публичного использования. Описываем модель таблицы трафика.
 *
 * @author gromiloff
 * */
data class TrafficModel(
    val packageName: String = "",
    val packageUid: Int = Int.MIN_VALUE,
    val timeStart: Long = 0L,
    val timeEnd: Long = 0L,
    val rxBytes: Long = 0L,
    val txBytes: Long = 0L,
    val networkType: Int = Int.MIN_VALUE,
)