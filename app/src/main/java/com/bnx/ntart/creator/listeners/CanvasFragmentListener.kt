package com.bnx.ntart.creator.listeners

import com.bnx.ntart.creator.models.Coordinates

interface CanvasFragmentListener {
    fun onPixelTapped(coordinatesTapped: Coordinates)
    fun onActionUp()
}