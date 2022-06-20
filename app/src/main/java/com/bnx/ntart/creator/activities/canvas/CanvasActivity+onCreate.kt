package com.bnx.ntart.creator.activities.canvas

fun CanvasActivity.onCreate() {
    getExtras()
    setUpFragment()
    setBindings()
    initSharedPreferenceObject()
}