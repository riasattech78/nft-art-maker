package com.bnx.ntart.creator.customviews.pixelgridview

import com.bnx.ntart.creator.models.BitmapAction
import com.bnx.ntart.creator.models.BitmapActionData
import com.bnx.ntart.creator.models.Coordinates

fun PixelGridView.extendedReplacePixelsByColor(colorToFind: Int, colorToReplace: Int) {
    currentBitmapAction = BitmapAction(mutableListOf(), true)

    for (i_1 in 0 until pixelGridViewBitmap.width) {
        for (i_2 in 0 until pixelGridViewBitmap.height) {
            currentBitmapAction!!.actionData.add(
                BitmapActionData(
                Coordinates(i_1, i_2),
                pixelGridViewBitmap.getPixel(i_1, i_2),
            )
            )

            if (pixelGridViewBitmap.getPixel(i_1, i_2) == colorToFind) {
                pixelGridViewBitmap.setPixel(i_1, i_2, colorToReplace)
            }
        }
    }
    invalidate()
}