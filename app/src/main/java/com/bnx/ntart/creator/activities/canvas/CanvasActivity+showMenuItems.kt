package com.bnx.ntart.creator.activities.canvas

fun showMenuItems() {
    for (i in 0 until menu.size()) {
        menu.getItem(i).isVisible = true
    }
}