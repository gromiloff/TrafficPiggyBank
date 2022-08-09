package lang

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import lang.prefs.LangPrefHelper

abstract class LangActivity : AppCompatActivity() {
    private var localizableContext: Context? = null

    override fun attachBaseContext(base: Context) {
        localizableContext = LangContextWrapper.newContext(base, getLocaleString())
        super.attachBaseContext(base)
    }

    override fun onConfigurationChanged(config: Configuration) {
        val newConfig = Configuration(config)
        // надо переопределить конфиг и установить в него правильную локаль
        LangContextWrapper.configChange(getLocaleString(), newConfig)
        localizableContext = LangContextWrapper.newContext(baseContext, getLocaleString())
        super.onConfigurationChanged(newConfig)
    }

    override fun getAssets(): AssetManager = localizableContext!!.assets
    override fun getResources(): Resources = localizableContext!!.resources

    private fun getLocaleString() = LangPrefHelper.getCurrentLocale()

    override fun onDestroy() {
        localizableContext = null
        super.onDestroy()
    }
}
