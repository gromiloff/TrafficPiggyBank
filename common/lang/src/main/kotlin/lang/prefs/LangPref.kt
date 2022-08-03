package lang.prefs

import android.annotation.SuppressLint
import android.content.Context
import gromiloff.prefs.PrefEnum
import gromiloff.prefs.PublicEmptyImpl
import gromiloff.prefs.PublicPref
import java.util.*

internal object LangPref {
    private var instance : PublicPref? = null

    fun create(context: Context) {
        instance = PublicEmptyImpl(context, "localize_app")
    }

    fun getCurrentLocale() : String = instance!!.getString(AppPrefLocaleImpl.CurrentLocale)!!

    fun setNewLocale() = instance!!.setString(AppPrefLocaleImpl.CurrentLocale, "")
}


@SuppressLint("ConstantLocale")
private enum class AppPrefLocaleImpl(override val defaultValue: String) : PrefEnum<String> {
    CurrentLocale(Locale.getDefault().language)
}
