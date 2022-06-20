package com.bnx.ntart.creator.fragments.colorpicker

import android.graphics.Color
import com.bnx.ntart.databinding.FragmentColorPickerBinding
import com.bnx.ntart.creator.fragments.colorpicker.picker.ColorPickerPickerFragment
import com.bnx.ntart.creator.fragments.colorpicker.rgb.RGBColorPickerFragment
import com.bnx.ntart.creator.listeners.ColorPickerFragmentListener

var binding_: FragmentColorPickerBinding? = null

var oldColor_ = Color.BLACK
var colorPaletteMode_: Boolean = false

val binding get() = binding_!!

lateinit var caller: ColorPickerFragmentListener

var rgbFragmentInstance: RGBColorPickerFragment? = null
var pickerFragmentInstance: ColorPickerPickerFragment? = null
