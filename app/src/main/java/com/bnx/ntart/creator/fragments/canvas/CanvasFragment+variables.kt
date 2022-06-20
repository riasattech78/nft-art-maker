package com.bnx.ntart.creator.fragments.canvas

import com.bnx.ntart.creator.customviews.pixelgridview.PixelGridView
import com.bnx.ntart.databinding.FragmentCanvasBinding
import com.bnx.ntart.creator.listeners.CanvasFragmentListener

var binding_: FragmentCanvasBinding? = null

val binding get() = binding_!!

lateinit var caller: CanvasFragmentListener

lateinit var myCanvasViewInstance: PixelGridView