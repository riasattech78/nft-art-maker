package com.bnx.ntart.creator.fragments.colorpicker

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bnx.ntart.creator.listeners.ColorPickerFragmentListener
import com.bnx.ntart.databinding.FragmentColorPickerBinding

class ColorPickerFragment(private var oldColor: Int, private val colorPaletteMode: Boolean = false) : Fragment() {
    private fun instantiateVariables() {
        oldColor_ = oldColor
        colorPaletteMode_ = colorPaletteMode
    }

    companion object {
        fun newInstance(oldColor: Int, colorPaletteMode: Boolean = false) = ColorPickerFragment(oldColor, colorPaletteMode)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ColorPickerFragmentListener) caller = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding_ = FragmentColorPickerBinding.inflate(inflater, container, false)

        instantiateVariables()
        setOnClickListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding_ = null
    }
}