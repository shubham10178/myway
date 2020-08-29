package com.fluper.seeway.utilitarianFiles

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceUtils
private constructor(val context: Context) {
    val TAG = SharedPreferenceUtils::class.java.simpleName

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PreferenceName, Context.MODE_PRIVATE)
    private val persistableSharedPreferences: SharedPreferences =
        context.getSharedPreferences(Constants.PersistablePreferenceName, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val persistableEditor: SharedPreferences.Editor = persistableSharedPreferences.edit()

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreferenceUtils? = null
        fun getInstance(ctx: Context): SharedPreferenceUtils {
            if (instance == null) {
                instance = SharedPreferenceUtils(ctx)
            }
            return instance!!
        }
    }

    var isLoggedIn: Boolean
        get() = sharedPreferences["isLoggedIn", false]!!
        set(value) = sharedPreferences.set("isLoggedIn", value)

    var isSplashIn: Boolean
        get() = sharedPreferences["isSplashIn", false]!!
        set(value) = sharedPreferences.set("isSplashIn", value)

    var selectedLanguage: String
        get() = sharedPreferences["selectedLanguage", ""]!!
        set(value) = sharedPreferences.set("selectedLanguage", value)

    var accessToken: String?
        get() = sharedPreferences["accessToken", ""]!!
        set(value) = sharedPreferences.set("accessToken", value)

    var userOtp: String?
        get() = sharedPreferences["userOtp", ""]!!
        set(value) = sharedPreferences.set("userOtp", value)

    var isEmailVerified: Int?
        get() = sharedPreferences["isEmailVerified", 0]!!
        set(value) = sharedPreferences.set("isEmailVerified", value)

    var isemailVerified: String?
        get() = sharedPreferences["isemailVerified", ""]!!
        set(value) = sharedPreferences.set("isemailVerified", value)

    var userId: String
        get() = sharedPreferences["userId", ""]!!
        set(value) = sharedPreferences.set("userId", value)

    var userSocialId: String
        get() = sharedPreferences["userSocialId", ""]!!
        set(value) = sharedPreferences.set("userSocialId", value)

    var userSocialType: String
        get() = sharedPreferences["userSocialType", ""]!!
        set(value) = sharedPreferences.set("userSocialType", value)

    var tokenType: String
        get() = sharedPreferences["tokenType", ""]!!
        set(value) = sharedPreferences.set("tokenType", value)

    var profileImage: String
        get() = sharedPreferences["profileImage", ""]!!
        set(value) = sharedPreferences.set("profileImage", value)

    var userEmailId: String
        get() = sharedPreferences["userEmailId", ""]!!
        set(value) = sharedPreferences.set("userEmailId", value)

    var userMobile: String
        get() = sharedPreferences["userMobile", ""]!!
        set(value) = sharedPreferences.set("userMobile", value)

    var userCountryCode: String
        get() = sharedPreferences["userCountryCode", ""]!!
        set(value) = sharedPreferences.set("userCountryCode", value)

    var userType: String
        get() = sharedPreferences["userType", ""]!!
        set(value) = sharedPreferences.set("userType", value)

    var userName: String
        get() = sharedPreferences["userName", ""]!!
        set(value) = sharedPreferences.set("userName", value)

    var userFirstName: String
        get() = sharedPreferences["userFirstName", ""]!!
        set(value) = sharedPreferences.set("userFirstName", value)

    var userSocialFirstName: String
        get() = sharedPreferences["userSocialFirstName", ""]!!
        set(value) = sharedPreferences.set("userSocialFirstName", value)

    var userSocialLastName: String
        get() = sharedPreferences["userSocialLastName", ""]!!
        set(value) = sharedPreferences.set("userSocialLastName", value)

    var userLastName: String
        get() = sharedPreferences["userLastName", ""]!!
        set(value) = sharedPreferences.set("userLastName", value)

    var userPassword: String
        get() = sharedPreferences["userPassword", ""]!!
        set(value) = sharedPreferences.set("userPassword", value)

    var userConfirmPassword: String
        get() = sharedPreferences["userConfirmPassword", ""]!!
        set(value) = sharedPreferences.set("userConfirmPassword", value)

    var latitude: String?
        get() = persistableSharedPreferences["latitude", "0.0"]!!
        set(value) = persistableSharedPreferences.set("latitude", value)

    var longitude: String?
        get() = persistableSharedPreferences["longitude", "0.0"]!!
        set(value) = persistableSharedPreferences.set("longitude", value)

    var firstRun: Boolean
        get() = persistableSharedPreferences["firstRun", true]!!
        set(value) = persistableSharedPreferences.set("firstRun", value)

    var deviceUniqueId: String?
        get() = persistableSharedPreferences["deviceUniqueId", ""]!!
        set(value) = persistableSharedPreferences.set("deviceUniqueId", value)

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> Log.e(TAG, "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun deletePreferences() {
        editor.clear()
        editor.apply()
    }

    fun deletePersistablePreferences() {
        persistableEditor.clear()
        persistableEditor.apply()
    }
}
