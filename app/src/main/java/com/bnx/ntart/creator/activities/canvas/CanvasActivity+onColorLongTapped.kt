package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.adapters.ColorPickerAdapter
import com.bnx.ntart.creator.converters.JsonConverter
import com.bnx.ntart.creator.database.AppData
import com.bnx.ntart.creator.models.ColorPalette

fun CanvasActivity.extendedOnColorLongTapped(objId: Int, color: Int) {
    var it:List<ColorPalette> = AppData.colorPalettesDB.colorPalettesDao().getListColorPalettes()

    for (data in it) {
        if (data.objId == objId) {
            fromDB = data
            break
        }
    }

    val newData = JsonConverter.convertJsonStringToListOfInt(fromDB!!.colorPaletteColorData).toMutableList()
    newData.remove(color)
    AppData.colorPalettesDB.colorPalettesDao().updateColorPaletteColorData(JsonConverter.convertListOfIntToJsonString(newData.toList()), fromDB!!.objId)
    AppData.colorPalettesDB.colorPalettesDao().getAllColorPalettes().observe(this) {
        binding.activityCanvasColorPickerRecyclerView.adapter = ColorPickerAdapter(fromDB!!, this)
        binding.activityCanvasColorPickerRecyclerView.scrollToPosition(
            JsonConverter.convertJsonStringToListOfInt(
                fromDB!!.colorPaletteColorData
            ).size
        )
    }

}