package com.nordef.snapchatui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.ListView


class NonScrollListView : ListView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom)
        val params = layoutParams
        params.height = measuredHeight
    }
}