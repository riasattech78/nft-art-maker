package com.bnx.ntart.creator.activities.canvas

import android.widget.Toast
import com.bnx.ntart.creator.converters.BitmapConverter
import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.models.PixelArt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CanvasActivity.extendedSaveProject() {
    saved = true

    val bmp = getCoverImageBitmap()

    if (index == -1) {
        CoroutineScope(Dispatchers.IO).launch {
            AppData.pixelArtDB.pixelArtCreationsDao().insertPixelArt(
                PixelArt(BitmapConverter.convertBitmapToString(bmp), BitmapConverter.convertBitmapToString(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap), spanCount, spanCount, outerCanvasInstance.getCurrentRotation(), title.toString(),false))
        }
        onBackPressed()
    } else {
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.invalidate()

        AppData.pixelArtDB.pixelArtCreationsDao().apply {
            updatePixelArtCreationCoverBitmap(BitmapConverter.convertBitmapToString(bmp), currentPixelArtObj.objId)
            updatePixelArtCreationBitmap(BitmapConverter.convertBitmapToString(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap), currentPixelArtObj.objId)
            updatePixelArtCreationRotation(outerCanvasInstance.getCurrentRotation(), currentPixelArtObj.objId)
        }
        outerCanvasInstance.rotate(0)
        onBackPressed()
    }

    Toast.makeText(this, "Successfully saved $projectTitle", Toast.LENGTH_SHORT).show()
}