package api.table

import model.TrafficModel
import module.FeatureDataBaseApiModule

/**
 * Публичные функции для работы с таблицей данных с трафиком
 * @author gromiloff
 * */
interface TableTrafficApi : FeatureDataBaseApiModule {
    /** получить последнее время обновления базы данных. При синхронизации не проходим круг
     *  получения данных после текущего дня дальше в историю. считаем что все данные уже однозначно
     *  синхронизированы */
    fun lastUpdate() : Long

    /** создать новую запись или обновить предыдущую
     * @param packageName имя пакета
     * @param packageUid uid пакета (чтобы не тратить время на его получение)
     * @param asWifiNetwork true если данные получены через WiFi
     * @param rxBytes количество отправленный байтов
     * @param txBytes количество полученных байтов
     * @param timeStart начало интервала времени измерения
     * @param timeEnd окончание интернвала времени измерения
     * */
    suspend fun insertOrReplace(
        packageName: String,
        packageUid: Int,
        asWifiNetwork: Boolean,
        rxBytes: Long,
        txBytes: Long,
        timeStart: Long,
        timeEnd: Long
    )

    /** получить все данные из таблицы в виде [TrafficModel] при этом пакеты приложений будут
     * однозначно уникальны, а трафик суммирован. */
    suspend fun all() : List<TrafficModel>

    /** получить фильтрованные данные из таблицы в виде [TrafficModel] при этом пакеты приложений
     * будут однозначно уникальны, а трафик суммирован. */
    suspend fun all(interval : TrafficInterval) : List<TrafficModel>
}

/** Стурктура определяющая интервал для данных по трафику */
sealed class TrafficInterval {
    /** Стурктура определяющая интервал настоящего времени */
    sealed class Present : TrafficInterval() {
        /** Запрос на получение данных по сегодоняшнему дню */
        object Today : Present()
        /** Запрос на получение данных по текщей неделе */
        object Week :  Present()
        /** Запрос на получение данных по текущему месяцу */
        object Month : Present()
    }
    /** Стурктура определяющая интервал прошлого */
    sealed class Past : TrafficInterval() {
        /** Запрос на получение данных по полному дню в прошлом. количество [daysBeforeNow]
         * определяет насколько далеко от текущего дня идет запрос. Например значение 1 - это данные
         * за вчера*/
        data class Day(val daysBeforeNow : Int): Present()
        /** Запрос на получение данных по полной неделе в прошлом. количество [weeksBeforeNow]
         * определяет насколько далеко от текущей недели идет запрос. Например значение 1 - это
         * данные за прошлую неделю */
        data class Week(val weeksBeforeNow : Int): Present()
        /** Запрос на получение данных по полному месяцу в прошлом. количество [monthsBeforeNow]
         * определяет насколько далеко от текущего месяца идет запрос. Например значение 1 - это
         * данные за прошлый месяц */
        data class Month(val monthsBeforeNow : Int): Present()
    }
}