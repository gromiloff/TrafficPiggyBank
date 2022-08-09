package repository.crypto_wallet

import android.content.Context
import android.content.Intent
import android.net.Uri
import api.function.CryptoWalletApi
import com.squareup.moshi.Moshi
import files.createFile
import okhttp3.OkHttpClient
import org.walletconnect.Session
import org.walletconnect.impls.*
import timber.log.Timber

/**
 * Реализация логики работы с метамаском
 * */
internal class CryptoWalletImpl(
    private val moshi: Moshi,
    private val bridge: BridgeServer,
    private val config: Session.Config,
) : CryptoWalletApi, Session.Callback {
    private var session: Session? = null

    //region [MetamaskApi] реализация

    override fun checkApprovedAccount(context : Context) = !getSession(context).approvedAccounts().isNullOrEmpty()

    override fun start(context : Context) {
        getSession(context).apply {
            offer()
            addCallback(this@CryptoWalletImpl)
        }

        val intent = context.packageManager.getLaunchIntentForPackage("io.metamask")
        if(intent == null) {
            context.startActivity(Intent(Intent.ACTION_VIEW).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                data = Uri.parse("http://play.google.com/store/apps/details?id=io.metamask");
            })
        } else {
            context.startActivity(intent.apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(config.toWCUri())
                categories.clear()
                flags = 0
            })
        }
    }

    override fun stop() {
        session?.let {
            it.removeCallback(this)
            it.kill()
        }
        session = null
    }
    //endregion

    //region [Session.Callback] реализация
    override fun onMethodCall(call: Session.MethodCall) {
        Timber.d("onMethodCall > $call")
    }

    override fun onStatus(status: Session.Status) {
        Timber.d("onStatus > $status")
    }
    //endregion

    private fun getSession(context : Context) : Session {
        session = WCSession(config.toFullyQualifiedConfig(),
            MoshiPayloadAdapter(moshi),
            FileWCSessionStore(
                createFile(context, "session_store.json"), moshi
            ),
            OkHttpTransport.Builder(OkHttpClient.Builder().build(), moshi),
            Session.PeerMeta(name = "This mega app")
        )
        return session!!
    }
}
