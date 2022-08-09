package api.function

import api.ll.RequestPermissionApi

/**
 * Публичные функции для работы с синхронизацией данных
 * @author gromiloff
 * */
interface TrafficWorkApi : RequestPermissionApi {
    /** стартовая точка синхронизации данных
     * Синхронизация считается с времени прследней если она была, либо только за сегодня
     * до текущего времени */
    suspend fun start()
}