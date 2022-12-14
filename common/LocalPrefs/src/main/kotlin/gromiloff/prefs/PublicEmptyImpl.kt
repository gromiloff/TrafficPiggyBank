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
        pref.getString(key.name, defaultValue ?: key.defaultValue as String).apply {
            Timber.d("> getString [$key] = $this")
        }
    override fun <T : PrefEnum<*>> getInt(key: T, defaultValue : Int?) =
        pref.getInt(key.name, defaultValue ?: key.defaultValue as Int).apply {
            Timber.d("> getInt [$key] = $this")
        }
    override fun <T : PrefEnum<*>> getBoolean(key: T, defaultValue : Boolean) =
        pref.getBoolean(key.name, defaultValue).apply {
            Timber.d("> getBoolean [$key] = $this")
        }
    override fun <T : PrefEnum<*>> getFloat(key: T, defaultValue : Float) =
        pref.getFloat(key.name, defaultValue).apply {
            Timber.d("> getFloat [$key] = $this")
        }
    override fun <T : PrefEnum<*>> getLong(key: T, defaultValue : Long) =
        pref.getLong(key.name, defaultValue).apply {
            Timber.d("> getLong [$key] = $this")
        }

    /* setter for fields */
    override fun <T : PrefEnum<*>> setBoolean(key: T, value : Boolean) {
        store { it.putBoolean(key.toString(), value) }
        Timber.d("> setBoolean [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setFloat(key: T, value : Float) {
        store { it.putFloat(key.toString(), value) }
        Timber.d("> setFloat [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setInt(key: T, value : Int) {
        store { it.putInt(key.toString(), value) }
        Timber.d("> setInt [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setLong(key: T, value : Long) {
        store { it.putLong(key.toString(), value) }
        Timber.d("> setLong [$key] = $value")
    }
    override fun <T : PrefEnum<*>> setString(key: T, value : String?) {
        store { it.putString(key.toString(), value) }
        Timber.d("> setString [$key] = $value")
    }

    override fun <T : PrefEnum<*>> resetString(key: T){
        setString(key, key.defaultValue as String)
        Timber.d("> resetString [$key]")
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
        private val prefs = HashMap<String, PublicEmptyImpl>()

        fun init(context: Context, name : String) {
            if(prefs.containsKey(name)) throw RuntimeException("name [$name] already in pref map")
            prefs[name] = PublicEmptyImpl(context, name)
        }

        fun get(name : String) = prefs[name]
    }
}
