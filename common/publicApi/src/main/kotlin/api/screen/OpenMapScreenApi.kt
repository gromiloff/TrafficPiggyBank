package api.screen

import android.content.Context
import api.model.data.Box

/**
 * Публичный интерфейс для работы с экранами карты
 *
 * @author gromiloff
 * */
interface OpenMapScreenApi {
    /** запустить экран карты с дополнительными настройками
     * @param box коробка если указана, которую пользователю надо открывать и сгенерировать задание
     * */
    fun launch(context : Context, box: Box? = null)
}