package impl

import android.content.Context
import gromiloff.prefs.PublicEmptyImpl
import lang.LangApplication
import marketplace.ToastModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
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
                ToastModule.create(this@App),
            )
        }

        Timber.plant(Timber.DebugTree())
    }
}