package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.R
import com.bnx.ntart.creator.fragments.outercanvas.OuterCanvasFragment

fun CanvasActivity.setUpFragment() {
    outerCanvasInstance = if (index == -1) {
        OuterCanvasFragment.newInstance(spanCount)
    } else {
        OuterCanvasFragment.newInstance(spanCount, true)
    }
    supportFragmentManager.beginTransaction().add(R.id.activityCanvas_outerCanvasFragmentHost, outerCanvasInstance).commit()
}