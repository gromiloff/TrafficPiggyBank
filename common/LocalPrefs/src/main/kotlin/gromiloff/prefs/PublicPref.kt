@file:Suppress("unused")

package gromiloff.prefs

import android.content.SharedPreferences

interface PublicPref {
    /* getter for fields */
    fun <T : PrefEnum<*>> getString(key: T, defaultValue : String? = null) : String?
    fun <T : PrefEnum<*>> getInt(key: T, defaultValue : Int? = null) : Int
    fun <T : PrefEnum<*>> getBoolean(key: T, defaultValue : Boolean = false) : Boolean
    fun <T : PrefEnum<*>> getFloat(key: T, defaultValue : Float = 0f) : Float
    fun <T : PrefEnum<*>> getLong(key: T, defaultValue : Long = 0L) : Long

    /* setter for fields */
    fun <T : PrefEnum<*>> setString(key: T, value : String?)
    fun <T : PrefEnum<*>> setBoolean(key: T, value : Boolean)
    fun <T : PrefEnum<*>> setFloat(key: T, value : Float)
    fun <T : PrefEnum<*>> setInt(key: T, value : Int)
    fun <T : PrefEnum<*>> setLong(key: T, value : Long)

    fun <T : PrefEnum<*>> resetString(key: T)

    fun getPref() : SharedPreferences
}
