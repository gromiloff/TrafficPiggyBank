package firebase

import android.content.Context
import api.firebase.FireStoreApi
import api.firebase.FirebaseAuthApi
import api.firebase.RemoteConfigApi
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import module.FeatureModule
import org.koin.dsl.module

/**
 * Определение внешних зависимостей для системы аналитики Firebase и сбора crash reports
 * */
object FirebaseModule : FeatureModule {
    override fun create(context: Context) = module {
        FirebaseApp.initializeApp(context)

        single<RemoteConfigApi> {
            ConfigPref.apply { create(context) }
        }

        single<FireStoreApi> {
            FireStoreImpl
        }

        single<FirebaseAuthApi> {
            FirebaseAuthImpl
        }

        single {
            FirebaseCrashlytics.getInstance()
        }
    }
}