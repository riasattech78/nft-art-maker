package com.bnx.ntart.creator.activities.canvas

import android.content.Context
import com.bnx.ntart.data.SharedPref

fun CanvasActivity.initSharedPreferenceObject() {
    sharedPreferenceObject = this.getPreferences(Context.MODE_PRIVATE)
    sharedPref = SharedPref(this)
}