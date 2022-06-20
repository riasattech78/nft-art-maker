package com.bnx.ntart.creator.customviews.pixelgridview

import com.bnx.ntart.creator.activities.canvas.outerCanvasInstance
import com.bnx.ntart.creator.models.BitmapActionData
import com.bnx.ntart.creator.models.Coordinates

fun PixelGridView.extendedOverrideSetPixel(x: Int, y: Int, color: Int) {
    val coordinates = Coordinates(x, y)
    if (coordinatesInCanvasBounds(coordinates)) {
        pixelGridViewBitmap.setPixel(coordinates.x, coordinates.y, color)
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!.actionData.add(BitmapActionData(coordinates, pixelGridViewBitmap.getPixel(coordinates.x, coordinates.y)))
    }
}