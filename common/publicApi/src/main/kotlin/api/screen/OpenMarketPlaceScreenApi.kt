package api.screen

import android.content.Context

/**
 * Публичный интерфейс для работы с экранами магазина
 *
 * @author gromiloff
 * */
interface OpenMarketPlaceScreenApi {
    // открываем магазин
    fun launch(context : Context)
}