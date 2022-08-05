package login

import android.content.Context
import api.screen.OpenLoginScreenApi
import module.FeatureModule
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Описание зависимостей предоставляемых модулем
 * @author gromiloff
 * * */
object LoginModule : FeatureModule {
    override fun create(context: Context): Module = module {
        single<OpenLoginScreenApi> { LoginPrivateImpl }
    }
}