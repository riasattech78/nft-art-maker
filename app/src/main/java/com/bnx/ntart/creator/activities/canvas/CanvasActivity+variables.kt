package com.bnx.ntart.creator.activities.canvas

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.Menu
import androidx.fragment.app.Fragment
import com.bnx.ntart.creator.adapters.ColorPickerAdapter
import com.bnx.ntart.databinding.ActivityCanvasBinding
import com.bnx.ntart.creator.fragments.colorpicker.ColorPickerFragment
import com.bnx.ntart.creator.fragments.outercanvas.OuterCanvasFragment
import com.bnx.ntart.creator.fragments.tools.ToolsFragment
import com.bnx.ntart.creator.models.ColorPalette
import com.bnx.ntart.creator.models.PixelArt
import com.bnx.ntart.data.SharedPref

lateinit var binding: ActivityCanvasBinding
var index: Int? = null

var primaryColor: Int = Color.BLACK
var secondaryColor: Int = Color.BLUE
var spanCount = 5
var isPrimaryColorSelected = true

var isSelected = false
var background: Drawable? = null

var currentFragmentInstance: Fragment? = null

var sharedPref: SharedPref? = null

lateinit var currentPixelArtObj: PixelArt

enum class Tools {
    PENCIL_TOOL,
    FILL_TOOL,
    HORIZONTAL_MIRROR_TOOL,
    VERTICAL_MIRROR_TOOL,
    LINE_TOOL,
    RECTANGLE_TOOL,
    OUTLINED_RECTANGLE_TOOL,
    COLOR_PICKER_TOOL,
    ERASE_TOOL,
    PALETTE_TOOL
}

var currentTool: Tools = Tools.PENCIL_TOOL

var saved = true

lateinit var colorPickerFragmentInstance: ColorPickerFragment
lateinit var outerCanvasInstance: OuterCanvasFragment

var colorPickerAdapter : ColorPickerAdapter? = null
var selectedColorPalette: ColorPalette? = null

lateinit var menu: Menu

var toolsFragmentInstance: ToolsFragment? = null

var lineMode_hasLetGo = false
var rectangleMode_hasLetGo = false

var projectTitle: String? = null

lateinit var sharedPreferenceObject: SharedPreferences


