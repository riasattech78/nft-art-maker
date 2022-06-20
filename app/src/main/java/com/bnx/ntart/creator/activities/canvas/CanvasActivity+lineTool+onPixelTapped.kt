package com.bnx.ntart.creator.activities.canvas

import com.bnx.ntart.creator.algorithms.LineAlgorithm
import com.bnx.ntart.creator.algorithms.AlgorithmInfoParameter
import com.bnx.ntart.creator.models.Coordinates

fun CanvasActivity.lineToolOnPixelTapped(coordinatesTapped: Coordinates) {
    val lineAlgorithmInstance = LineAlgorithm(AlgorithmInfoParameter(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap, outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!, getSelectedColor()))

    if (!lineMode_hasLetGo) {
        if (!first) outerCanvasInstance.canvasFragment.myCanvasViewInstance.undo()
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.bitmapActionData.add(outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!)
        first = false
    } else {
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction = null
        lineMode_hasLetGo = false
        first = true
    }

    if (lineOrigin == null) {
        lineOrigin = coordinatesTapped
    } else {
        lineAlgorithmInstance.compute(lineOrigin!!, coordinatesTapped)
    }
}