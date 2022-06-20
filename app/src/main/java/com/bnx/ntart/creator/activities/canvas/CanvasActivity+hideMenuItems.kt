package com.bnx.ntart.creator.activities.canvas

fun hideMenuItems() {
    for (i in 0 until menu.size()) {
        menu.getItem(i).isVisible = false
    }
}