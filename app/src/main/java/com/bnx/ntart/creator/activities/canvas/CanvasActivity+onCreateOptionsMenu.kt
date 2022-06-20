package com.bnx.ntart.creator.activities.canvas

import android.view.Menu
import com.bnx.ntart.R

fun CanvasActivity.extendedOnCreateOptionsMenu(_menu: Menu?): Boolean {
    val inflater = menuInflater
    inflater.inflate(R.menu.app_menu, _menu)

    if (_menu != null) {
        menu = _menu
    }

    if (index != -1) setMenuItemIcon(_menu!!.getItem(4), R.drawable.ic_baseline_save_24, "Save")

    return true
}