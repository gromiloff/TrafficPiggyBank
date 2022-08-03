package lang

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.*


class LangContextWrapper(base: Context) : ContextWrapper(base) {
    companion object {
        private fun getLocale(localeString : String) : Locale {
            val request = localeString.lowercase()
            val system = Resources.getSystem().configuration.locales[0].language.lowercase()
            return Locale(request.ifBlank { system })
        }

        @Suppress("DEPRECATION")
        @SuppressLint("ObsoleteSdkInt")
        fun newContext(context: Context, localeString : String): Context {
            val newLocale = getLocale(localeString)
            val res = context.resources
            val configuration = res.configuration
            configuration.setLocale(newLocale)

            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                    val localeList = LocaleList(newLocale)
                    LocaleList.setDefault(localeList)
                    configuration.setLocales(localeList)
                    ContextWrapper(context.createConfigurationContext(configuration))
                }
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 -> {
                    ContextWrapper(context.createConfigurationContext(configuration))
                }
                else -> {
                    res.updateConfiguration(configuration, res.displayMetrics)
                    ContextWrapper(context)
                }

            }
        }

        fun configChange(localeString : String, configuration : Configuration) {
            val newLocale = getLocale(localeString)
            configuration.setLocale(newLocale)
        }
    }
}