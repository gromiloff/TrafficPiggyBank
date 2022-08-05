package repository.sync

import android.content.Context
import api.function.SyncApi
import module.FeatureModule
import org.koin.dsl.module

/**
 * Описание зависимостей предоставляемых модулем синхронизации данных
 * */
object SyncModule : FeatureModule {
    override fun create(context: Context) = module {
        single<SyncApi> {
            SyncImpl
        }
    }
}