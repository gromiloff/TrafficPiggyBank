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

    // todo добавить метод получения трафика по интервалам : сегодня (настоящее), день (прошлое), неделя (прошлое), месяц (прошлое)
}