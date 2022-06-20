package com.bnx.ntart.creator.listeners

import com.bnx.ntart.creator.models.PixelArt

interface RecentCreationsListener {
    fun onCreationTapped(creationTapped: PixelArt)
    fun onCreationLongTapped(creationTapped: PixelArt)
}