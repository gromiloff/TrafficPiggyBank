package api.function

import android.content.Context
import android.widget.Toast
import api.model.text.TextHolder

/**
 * Публичный интерфейс для работы с настройками и показами [Toast] из единой точки
 *
 * @author gromiloff
 * */
interface ShowToastApi {
    // показать обычный текст в информере
    fun show(context : Context, text : TextHolder)
    // показать специальный информер что функционал еще не готов
    fun showNotImplement(context: Context)
}