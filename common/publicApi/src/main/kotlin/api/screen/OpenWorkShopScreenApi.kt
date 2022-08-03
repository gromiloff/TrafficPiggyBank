package api.screen

import android.content.Context

/**
 * Публичный интерфейс для работы с экранами мастерской
 *
 * @author gromiloff
 * */
interface OpenWorkShopScreenApi {
    // открываем магазин
    fun launch(context : Context)
    // открываем карточкку бокса по уникальному ключу
    //fun openBoxCard(fm : FragmentManager, itemId: String)
}