package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.models.ColorPalette

var fromDB: ColorPalette? = null

fun CanvasActivity.extendedOnAddColorTapped(colorPalette: ColorPalette) {
    hideMenuItems()

    AppData.colorPalettesDB.colorPalettesDao().getAllColorPalettes().observe(this) {
        for (data in it) {
            if (data.objId == colorPalette.objId) {
                fromDB = data
                break
            }
        }
    }
    openColorPickerDialog(true)
}