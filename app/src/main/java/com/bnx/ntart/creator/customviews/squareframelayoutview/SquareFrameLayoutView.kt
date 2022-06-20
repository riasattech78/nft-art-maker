package com.bnx.ntart.creator.customviews.squareframelayoutview

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class SquareFrameLayoutView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributes: AttributeSet) : super(context, attributes)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        setMeasuredDimension(measuredWidth, width)
    }
}