package api.function

import android.content.Context
import kotlin.jvm.Throws

/**
 * Публичные функции для работы с метамаском
 * @author gromiloff
 * */
interface CryptoWalletApi {
    /** получить информацию имеется ли связь между приложением и внешним крипто кошельком (предоставлены номера кошельков) */
    fun checkApprovedAccount(context : Context) : Boolean
    /** запустить механизм подключения к внешнему криптокошельку
     *
     * проверено и работает на Metamask
     *
     * не работает 1inch
     * */
    @Throws(UninitializedPropertyAccessException::class)
    fun start(context : Context)
    /** отсановить все действия */
    fun stop()
}