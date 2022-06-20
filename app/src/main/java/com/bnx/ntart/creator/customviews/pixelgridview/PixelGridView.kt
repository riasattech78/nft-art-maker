package com.bnx.ntart.creator.customviews.pixelgridview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.View
import com.bnx.ntart.creator.listeners.CanvasFragmentListener
import com.bnx.ntart.creator.models.BitmapAction
import com.bnx.ntart.creator.models.Coordinates

@SuppressLint("ViewConstructor")
class PixelGridView (context: Context, var canvasSize: Int, private var isEmpty: Boolean) : View(context) {
    lateinit var pixelGridViewCanvas: Canvas
    lateinit var pixelGridViewBitmap: Bitmap

    var scaleWidth = 0f
    var scaleHeight = 0f

    var prevX: Int? = null
    var prevY: Int? = null

    val bitmapActionData: MutableList<BitmapAction> = mutableListOf()
    var currentBitmapAction: BitmapAction? = null

    var pixelPerfectMode: Boolean = false

    lateinit var caller: CanvasFragmentListener

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        caller = context as CanvasFragmentListener

        if (::pixelGridViewBitmap.isInitialized) {
            //pixelGridViewBitmap.recycle()
        }

        if (!isEmpty) {
            pixelGridViewBitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888)
            pixelGridViewCanvas = Canvas(pixelGridViewBitmap)
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent) = extendedDispatchTouchEvent(event)

    fun undo() = extendedUndo()

    fun clearCanvas() = extendedClearCanvas()

    fun overrideSetPixel(x: Int, y: Int, color: Int) = extendedOverrideSetPixel(x, y, color)

    fun replaceBitmap(newBitmap: Bitmap) = extendedReplaceBitmap(newBitmap)

    fun saveAsImage(format: Bitmap.CompressFormat) = extendedSaveAsImage(format)

    private fun calculateMatrix(bm: Bitmap, newHeight: Int, newWidth: Int) = extendedCalculateMatrix(bm, newHeight, newWidth)

    fun coordinatesInCanvasBounds(coordinates: Coordinates) = (coordinates.x in 0 until canvasSize && coordinates.y in 0 until canvasSize)

    override fun onDraw(canvas: Canvas) {
        if (::pixelGridViewBitmap.isInitialized) {
            canvas.drawBitmap(pixelGridViewBitmap, calculateMatrix(pixelGridViewBitmap, this.width, this.width), null)
        }
    }
}
