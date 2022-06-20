package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.converters.BitmapConverter
import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.utility.IntConstants

fun CanvasActivity.replaceBitmapIfApplicable() {
    if (index != -1) {
        AppData.pixelArtDB.pixelArtCreationsDao().getAllPixelArtCreations("").observe(context) {
            currentPixelArtObj = it[index!!]
            outerCanvasInstance.canvasFragment.myCanvasViewInstance.replaceBitmap(BitmapConverter.convertStringToBitmap(currentPixelArtObj.bitmap)!!)
            outerCanvasInstance.rotate(it[index!!].rotation.toInt(), false)
            IntConstants.SPAN_COUNT = currentPixelArtObj.width
        }
    }
}