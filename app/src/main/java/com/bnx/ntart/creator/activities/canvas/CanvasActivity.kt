package com.bnx.ntart.creator.activities.canvas

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bnx.ntart.creator.fragments.colorpicker.ColorPickerFragment
import com.bnx.ntart.creator.listeners.*
import com.bnx.ntart.creator.models.ColorPalette
import com.bnx.ntart.creator.models.Coordinates

class CanvasActivity :
    AppCompatActivity(),
    CanvasFragmentListener,
    ColorPickerListener,
    ColorPickerFragmentListener,
    ToolsFragmentListener,
    ColorPalettesFragmentListener {

    var previousView: View? = null

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setUpRecyclerView()
        setOnClickListeners()
        setColors(this)
        replaceBitmapIfApplicable()
        prepareAds()
    }

    fun initColorPickerFragmentInstance(colorPaletteMode: Boolean) = ColorPickerFragment.newInstance(getSelectedColor(), colorPaletteMode)

    override fun onCreateOptionsMenu(menu: Menu?) = extendedOnCreateOptionsMenu(menu)

    override fun onOptionsItemSelected(item: MenuItem) = extendedOnOptionsItemSelected(item)

    fun getSelectedColor() = extendedGetSelectedColor()

    fun setPixelColor(color: Int) = if (isPrimaryColorSelected) setPrimaryPixelColor(color) else setSecondaryPixelColor(color)

    override fun onPause() {
        extendedOnPause()
        super.onPause()
    }

    override fun onPixelTapped(coordinatesTapped: Coordinates) = extendedOnPixelTapped(coordinatesTapped)

    override fun onActionUp() = extendedOnActionUp()

    override fun onColorTapped(colorTapped: Int, view: View) = extendedOnColorTapped(colorTapped, view)

    override fun onColorLongTapped(objId: Int, colorTapped: Int, view: View) = extendedOnColorLongTapped(objId, colorTapped)

    override fun onColorAdded(colorPalette: ColorPalette) = extendedOnAddColorTapped(colorPalette)

    override fun onDoneButtonPressed(selectedColor: Int, isColorPaletteMode: Boolean) = extendedOnDoneButtonPressed(selectedColor, isColorPaletteMode)

    override fun onBackPressed() = extendedOnBackPressed()

    override fun onToolTapped(toolName: String) = extendedOnToolTapped(toolName)

    override fun onColorPaletteTapped(selectedColorPalette: ColorPalette) = extendedOnColorPaletteTapped(selectedColorPalette)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        extendedOnRequestPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
