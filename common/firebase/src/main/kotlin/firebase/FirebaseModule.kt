package firebase

import android.content.Context
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

        single {
            FirebaseCrashlytics.getInstance()
        }
    }
}