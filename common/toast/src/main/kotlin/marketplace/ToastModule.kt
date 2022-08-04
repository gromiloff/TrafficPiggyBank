package marketplace

import android.content.Context
import api.function.ShowToastApi
import module.FeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Описание зависимостей предоставляемых модулем
 * @author gromiloff
 * * */
object ToastModule : FeatureModule {
    override fun create(context: Context): Module = module {
        single<ShowToastApi> { ToastPrivateImpl }
    }
}