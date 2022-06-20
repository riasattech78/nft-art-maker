package com.bnx.ntart.creator.fragments.colorpicker

import com.bnx.ntart.R
import com.bnx.ntart.creator.fragments.colorpicker.rgb.RGBColorPickerFragment

fun ColorPickerFragment.setOnClickListeners() {
    rgbFragmentInstance = RGBColorPickerFragment.newInstance()
    activity!!.supportFragmentManager.beginTransaction().add(R.id.fragmentColorPicker_tabLayoutFragmentHost, rgbFragmentInstance!!).commit()
}