package lang

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import lang.prefs.LangPrefHelper

abstract class LangApplication : Application() {
    private lateinit var localizableContext: Context

    override fun attachBaseContext(base: Context) {
        LangPrefHelper.create(base)
        localizableContext = LangContextWrapper.newContext(base, getLocaleString())
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(config: Configuration) {
        val newConfig = Configuration(config)
        // надо переопределить конфиг и установить в него правильную локаль
        getLocaleString().let {
            LangContextWrapper.configChange(it, newConfig)
            localizableContext = LangContextWrapper.newContext(baseContext, it)
        }
        super.onConfigurationChanged(newConfig)
    }

    override fun getAssets(): AssetManager = localizableContext.assets

    override fun getResources(): Resources = localizableContext.resources

    private fun getLocaleString() = LangPrefHelper.getCurrentLocale()
}