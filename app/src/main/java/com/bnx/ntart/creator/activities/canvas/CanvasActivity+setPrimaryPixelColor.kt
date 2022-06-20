package com.bnx.ntart.creator.activities.canvas

fun setPrimaryPixelColor(color: Int) {
    primaryColor = color
    binding.activityCanvasColorPrimaryView.setBackgroundColor(color)
}