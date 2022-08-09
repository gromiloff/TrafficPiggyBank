package impl

import android.content.Context
import firebase.FirebaseModule
import gromiloff.prefs.PublicEmptyImpl
import lang.LangApplication
import login.LoginModule
import marketplace.ToastModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import repository.crypto_wallet.CryptoWalletModule
import repository.operation.TrafficWorkModule
import table.traffic.TableTrafficModule
import timber.log.Timber

@Suppress("unused")
class App : LangApplication(), KoinComponent {
    override fun attachBaseContext(base: Context) {
        PublicEmptyImpl.init(base, "main")
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.INFO)
            androidContext(this@App)
            modules(
                FirebaseModule.create(this@App),

                ToastModule.create(this@App),

                TableTrafficModule.create(this@App),

                TrafficWorkModule.create(this@App),
                CryptoWalletModule.create(this@App),

                LoginModule.create(this@App),
            )
        }

        Timber.plant(Timber.DebugTree())
    }
}