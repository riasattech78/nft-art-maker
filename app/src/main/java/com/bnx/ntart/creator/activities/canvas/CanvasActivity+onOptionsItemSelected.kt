package com.bnx.ntart.creator.activities.canvas

import android.graphics.Bitmap
import android.view.MenuItem
import com.bnx.ntart.R
import com.bnx.ntart.utils.PermissionUtil

fun CanvasActivity.extendedOnOptionsItemSelected(item: MenuItem): Boolean {
    val zoomIncrement = 0.2f

    when (item.itemId) {
        R.id.zoom_out -> {
            outerCanvasInstance.cardViewParent.apply {
                if (outerCanvasInstance.cardViewParent.scaleX - zoomIncrement > 0.2f) {
                    scaleX -= zoomIncrement
                    scaleY -= zoomIncrement
                }
            }
        }
        R.id.zoom_in -> {
            outerCanvasInstance.cardViewParent.apply {
                scaleX += zoomIncrement
                scaleY += zoomIncrement
            }
        }
        R.id.save_project -> extendedSaveProject()

        R.id.undo -> extendedUndo()

        R.id.redo -> extendedRedo()

        R.id.export_to_png -> {
            if (!PermissionUtil().isStorageGranted(this)) {
                if (sharedPref!!.getNeverAskAgain(PermissionUtil().STORAGE)) {
                    PermissionUtil().showDialog(this)
                } else {
                    requestPermissions(PermissionUtil().PERMISSION_STORAGE, 500)
                }
                return true
            }

            outerCanvasInstance.canvasFragment.myCanvasViewInstance.saveAsImage(Bitmap.CompressFormat.PNG)
        }

        R.id.export_to_jpg -> {
            if (!PermissionUtil().isStorageGranted(this)) {
                if (sharedPref!!.getNeverAskAgain(PermissionUtil().STORAGE)) {
                    PermissionUtil().showDialog(this)
                } else {
                    requestPermissions(PermissionUtil().PERMISSION_STORAGE, 500)
                }
                return true
            }
            outerCanvasInstance.canvasFragment.myCanvasViewInstance.saveAsImage(Bitmap.CompressFormat.JPEG)
        }

        R.id.reset_zoom -> {
            resetZoom()
        }
    }
    return true
}