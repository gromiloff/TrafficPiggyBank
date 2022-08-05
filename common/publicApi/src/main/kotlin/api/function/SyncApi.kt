package api.function

import api.ll.RequestPermissionApi

/**
 * Публичные функции для работы с синхронизацией данных
 * @author gromiloff
 * */
interface SyncApi : RequestPermissionApi {
    /** стартовая точка синхронизации данных начиная с заданного [from] по [to] включительно */
    suspend fun start(from : Long = 0, to : Long = System.currentTimeMillis())
}