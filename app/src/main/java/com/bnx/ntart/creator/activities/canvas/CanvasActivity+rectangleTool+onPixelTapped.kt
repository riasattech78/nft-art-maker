package com.bnx.ntart.creator.activities.canvas

import android.graphics.drawable.ColorDrawable
import com.bnx.ntart.creator.algorithms.RectangleAlgorithm
import com.bnx.ntart.creator.algorithms.AlgorithmInfoParameter
import com.bnx.ntart.creator.models.Coordinates

fun CanvasActivity.rectangleToolOnPixelTapped(coordinatesTapped: Coordinates, hasBorder: Boolean) {

    val rectangleAlgorithmInstance: RectangleAlgorithm = if (!hasBorder) {
        RectangleAlgorithm(AlgorithmInfoParameter(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap,  outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!, getSelectedColor()))
    } else {
        RectangleAlgorithm(AlgorithmInfoParameter(outerCanvasInstance.canvasFragment.myCanvasViewInstance.pixelGridViewBitmap,  outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!, (binding.activityCanvasColorPrimaryView.background as ColorDrawable).color))
    }

    if (!rectangleMode_hasLetGo) {
        if (!first)  outerCanvasInstance.canvasFragment.myCanvasViewInstance.undo()
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.bitmapActionData.add( outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction!!)
        first = false
    } else {
        outerCanvasInstance.canvasFragment.myCanvasViewInstance.currentBitmapAction = null
        rectangleMode_hasLetGo = false
        first = true
    }

    if (rectangleOrigin == null) {
        rectangleOrigin = coordinatesTapped
    } else {
        rectangleAlgorithmInstance.compute(rectangleOrigin!!, coordinatesTapped)
    }
}