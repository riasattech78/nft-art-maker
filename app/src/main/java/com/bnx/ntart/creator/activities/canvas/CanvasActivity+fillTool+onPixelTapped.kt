package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.algorithms.AlgorithmInfoParameter
import com.bnx.ntart.creator.algorithms.FloodFillAlgorithm
import com.bnx.ntart.creator.models.Coordinates

fun CanvasActivity.fillToolOnPixelTapped(coordinatesTapped: Coordinates) {
    val floodFillAlgorithmInstance = FloodFillAlgorithm(AlgorithmInfoParameter(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap, outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!, getSelectedColor()))
    floodFillAlgorithmInstance.compute(Coordinates(coordinatesTapped.x, coordinatesTapped.y))
}