package com.bnx.ntart.creator.customviews.pixelgridview

import android.graphics.Color
import com.bnx.ntart.creator.activities.canvas.outerCanvasInstance
import com.bnx.ntart.creator.models.BitmapAction
import com.bnx.ntart.creator.models.BitmapActionData
import com.bnx.ntart.creator.models.Coordinates

fun PixelGridView.extendedApplyBitmapFilter(lambda: (Int) -> Int) {
    currentBitmapAction = BitmapAction(mutableListOf(), true)

    for (i_1 in 0 until pixelGridViewBitmap.width) {
        for (i_2 in 0 until pixelGridViewBitmap.height) {
            if (pixelGridViewBitmap.getPixel(i_1, i_2) != Color.TRANSPARENT) {
                val color = lambda(pixelGridViewBitmap.getPixel(i_1, i_2))

                currentBitmapAction!!.actionData.add(
                    BitmapActionData(
                    Coordinates(i_1, i_2),
                    pixelGridViewBitmap.getPixel(i_1, i_2),
                )
                )

                pixelGridViewBitmap.setPixel(i_1, i_2, color)
            }
        }
    }

    outerCanvasInstance.canvasFragment.myCanvasViewInstance.bitmapActionData.add(outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!)
    outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction = null

    invalidate()
}