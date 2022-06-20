package com.bnx.ntart.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {

    var prefs: SharedPreferences = context.getSharedPreferences("MAIN_PREF", Context.MODE_PRIVATE)

    /**
     * To save dialog permission state
     */
    fun setExported(value: Boolean) {
        prefs.edit().putBoolean("EXPORTED", value).apply()
    }

    fun getExported(): Boolean {
        return prefs.getBoolean("EXPORTED", false)
    }

    /**
     * To save first launch
     */
    fun setFirstLaunch(value: Boolean) {
        prefs.edit().putBoolean("FIRST", value).apply()
    }

    fun getFirstLaunch(): Boolean {
        return prefs.getBoolean("FIRST", true)
    }

    /**
     * To save dialog permission state
     */
    fun setNeverAskAgain(key: String?, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun getNeverAskAgain(key: String?): Boolean {
        return prefs.getBoolean(key, false)
    }

    // Preference for settings
    fun setPushNotification(value: Boolean) {
        prefs.edit().putBoolean("SETTINGS_PUSH_NOTIF", value).apply()
    }

    fun getPushNotification(): Boolean {
        return prefs.getBoolean("SETTINGS_PUSH_NOTIF", true)
    }

    fun setVibration(value: Boolean) {
        prefs.edit().putBoolean("SETTINGS_VIBRATION", value).apply()
    }

    fun getVibration(): Boolean {
        return prefs.getBoolean("SETTINGS_VIBRATION", true)
    }


    // Preference for first launch
    fun setIntersCounter(counter: Int) {
        prefs.edit().putInt("INTERS_COUNT", counter).apply()
    }

    fun getIntersCounter(): Int {
        return prefs.getInt("INTERS_COUNT", 0)
    }

    fun clearIntersCounter() {
        prefs.edit().putInt("INTERS_COUNT", 0).apply()
    }
}