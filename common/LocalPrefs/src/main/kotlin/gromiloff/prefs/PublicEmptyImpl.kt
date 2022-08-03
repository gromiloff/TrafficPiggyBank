@file:Suppress("unused")

package gromiloff.prefs

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Looper
import timber.log.Timber

@SuppressLint("CommitPrefEdits")
open class PublicEmptyImpl(context: Context, name : String) : PublicPref {
    private val pref = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private var prefObserver : ObserverValue? = null

    /* getter for fields */
    override fun <T : PrefEnum<*>> getString(key: T, defaultValue: String?) =
        pref.getString(key.name, defaultValue ?: key.defaultValue as String)
    override fun <T : PrefEnum<*>> getInt(key: T, defaultValue : Int?) =
        pref.getInt(key.name, defaultValue ?: key.defaultValue as Int)
    override fun <T : PrefEnum<*>> getBoolean(key: T, defaultValue : Boolean) =
        pref.getBoolean(key.name, defaultValue)
    override fun <T : PrefEnum<*>> getFloat(key: T, defaultValue : Float) =
        pref.getFloat(key.name, defaultValue)
    override fun <T : PrefEnum<*>> getLong(key: T, defaultValue : Long) =
        pref.getLong(key.name, defaultValue)

    /* setter for fields */
    override fun <T : PrefEnum<*>> setBoolean(key: T, value : Boolean) {
        store { it.putBoolean(key.toString(), value) }
        Timber.d("save new value for [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setFloat(key: T, value : Float) {
        store { it.putFloat(key.toString(), value) }
        Timber.d("save new value for [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setInt(key: T, value : Int) {
        store { it.putInt(key.toString(), value) }
        Timber.d("save new value for [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setLong(key: T, value : Long) {
        store { it.putLong(key.toString(), value) }
        Timber.d("save new value for [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setString(key: T, value : String?) {
        store { it.putString(key.toString(), value) }
        Timber.d("save new value for [$key] = $value")
    }

    override fun <T : PrefEnum<*>> resetString(key: T){
        setString(key, key.defaultValue as String)
        Timber.d("reset value for [$key]")
    }

    override fun getPref(): SharedPreferences = pref

    @SuppressLint("ApplySharedPref")
    private fun store(func : (editor : SharedPreferences.Editor) -> Unit) {
        pref.edit()?.apply {
            func(this)
            if (Looper.myLooper() == Looper.getMainLooper()) {
                apply()
            } else {
                commit()
            }
        }
    }

    companion object {
        private var globalAppPrefs : PublicEmptyImpl? = null

        fun init(context: Context, name : String) {
            globalAppPrefs = PublicEmptyImpl(context, name)
        }

        fun get() = globalAppPrefs!!
    }
}
