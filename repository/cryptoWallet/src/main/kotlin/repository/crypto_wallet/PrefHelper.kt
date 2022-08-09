package repository.crypto_wallet

import android.content.Context
import gromiloff.prefs.PrefEnum
import gromiloff.prefs.PublicEmptyImpl
import java.util.*

internal object PrefHelper {
    private const val PREF_NAME = "meta"

    fun create(context: Context) {
        PublicEmptyImpl.init(context, PREF_NAME)
    }

    /** генерируем уникальный [UUID] при первом запросе и запоминаем его для дальнейшей работы */
    fun getUuid() : String {
        val api = PublicEmptyImpl.get(PREF_NAME)!!
        var result = api.getString(AppPrefLocaleImpl.CurrentUuid)
        if(result.isNullOrBlank()) result = UUID.randomUUID().toString()
        api.setString(AppPrefLocaleImpl.CurrentUuid, result)
        return result
    }
}

private enum class AppPrefLocaleImpl(override val defaultValue: String) : PrefEnum<String> {
    CurrentUuid("")
}
