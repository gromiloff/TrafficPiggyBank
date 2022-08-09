package repository.crypto_wallet

import android.content.Context
import api.function.CryptoWalletApi
import com.squareup.moshi.Moshi
import files.createFile
import module.FeatureModule
import okhttp3.OkHttpClient
import org.koin.dsl.module
import org.komputing.khex.extensions.toNoPrefixHexString
import org.walletconnect.Session
import org.walletconnect.impls.FileWCSessionStore
import java.util.*

/**
 * Описание зависимостей предоставляемых модулем работы с метамаском
 * */
object CryptoWalletModule : FeatureModule {
    override fun create(context: Context) = module {
        val moshi = Moshi.Builder().build()

        PrefHelper.create(context)

        single<CryptoWalletApi> {
            CryptoWalletImpl(
                moshi,
                BridgeServer(moshi),
                Session.Config(
                    PrefHelper.getUuid(), "https://bridge.walletconnect.org",
                    ByteArray(32).also { Random().nextBytes(it) }.toNoPrefixHexString()
                )
            )
        }
    }
}