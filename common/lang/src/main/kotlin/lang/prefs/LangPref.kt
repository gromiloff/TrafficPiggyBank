package lang.prefs

import android.annotation.SuppressLint
import android.content.Context
import gromiloff.prefs.PrefEnum
import gromiloff.prefs.PublicEmptyImpl
import gromiloff.prefs.PublicPref
import java.util.*

internal object LangPrefHelper {
    private const val PREF_NAME = "localize_app"

    fun create(context: Context) {
        PublicEmptyImpl.init(context, PREF_NAME)
    }

    fun getCurrentLocale() : String = PublicEmptyImpl.get(PREF_NAME)!!.getString(AppPrefLocaleImpl.CurrentLocale)!!

    fun setNewLocale() = PublicEmptyImpl.get(PREF_NAME)!!.setString(AppPrefLocaleImpl.CurrentLocale, "")
}


@SuppressLint("ConstantLocale")
private enum class AppPrefLocaleImpl(override val defaultValue: String) : PrefEnum<String> {
    CurrentLocale(Locale.getDefault().language)
}
