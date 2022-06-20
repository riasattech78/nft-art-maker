package com.bnx.ntart.creator.customviews.transparentbackgroundview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.View

class TransparentBackgroundView(context: Context, private val canvasSize: Int) : View(context) {
    private lateinit var transparentBackgroundViewCanvas: Canvas
    private lateinit var transparentBackgroundViewBitmap: Bitmap

    var scaleWidth = 0f
    var scaleHeight = 0f

    private var isEvenCanvasSize = canvasSize % 2 == 0

    var color = Color.LTGRAY

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::transparentBackgroundViewBitmap.isInitialized) {
            transparentBackgroundViewBitmap.recycle()
        }

        transparentBackgroundViewBitmap = Bitmap.createBitmap(canvasSize, canvasSize, Bitmap.Config.ARGB_8888)
        transparentBackgroundViewCanvas = Canvas(transparentBackgroundViewBitmap)
    }

    private fun calculateMatrix(bm: Bitmap, newHeight: Int, newWidth: Int) = extendedCalculateMatrix(bm, newHeight, newWidth)

    override fun onDraw(canvas: Canvas) {
        if (::transparentBackgroundViewBitmap.isInitialized) {
            canvas.drawBitmap(transparentBackgroundViewBitmap, calculateMatrix(transparentBackgroundViewBitmap, this.width, this.width), null)

            for (i_1 in 0 until canvasSize) {
                for (i_2 in 0 until canvasSize) {
                    transparentBackgroundViewBitmap.setPixel(i_1, i_2, color)

                    color = if (!isEvenCanvasSize) {
                        if (color == Color.WHITE) {
                            Color.LTGRAY
                        } else {
                            Color.WHITE
                        }
                    } else {
                        if (color == Color.LTGRAY) {
                            Color.WHITE
                        } else {
                            Color.LTGRAY
                        }
                    }
                }

                color = if (!isEvenCanvasSize) {
                    if (color != Color.WHITE) {
                        Color.LTGRAY
                    } else {
                        Color.WHITE
                    }
                } else {
                    if (color != Color.LTGRAY) {
                        Color.LTGRAY
                    } else {
                        Color.WHITE
                    }
                }
            }
        }
    }
}