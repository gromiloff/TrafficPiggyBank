package table.traffic

import android.content.Context
import gromiloff.prefs.PrefEnum
import gromiloff.prefs.PublicEmptyImpl

internal object PrefHelper {
    private const val PREF_NAME = "traffic"

    fun create(context: Context) {
        PublicEmptyImpl.init(context, PREF_NAME)
    }

    /** получить последнее время синхронизации */
    fun get() = PublicEmptyImpl.get(PREF_NAME)!!.getLong(AppPrefLocaleImpl.LastTimeStump)

    /** установить последнее время синхронизации */
    fun set(time : Long) {
        PublicEmptyImpl.get(PREF_NAME)!!.setLong(AppPrefLocaleImpl.LastTimeStump, time)
    }
}

private enum class AppPrefLocaleImpl(override val defaultValue: Long) : PrefEnum<Long> {
    LastTimeStump(0L)
}
