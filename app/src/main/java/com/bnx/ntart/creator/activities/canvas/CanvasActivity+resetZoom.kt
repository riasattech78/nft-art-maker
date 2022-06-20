package com.bnx.ntart.creator.activities.canvas

fun resetZoom() {
    outerCanvasInstance.cardViewParent.apply {
        scaleX = 1f
        scaleY = 1f
    }
}