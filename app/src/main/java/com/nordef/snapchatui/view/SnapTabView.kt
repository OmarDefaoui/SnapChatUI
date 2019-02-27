package com.nordef.snapchatui.view

import android.animation.ArgbEvaluator
import android.content.Context
import android.content.res.Configuration
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.ImageView
import com.nordef.snapchatui.R


class SnapTabView : FrameLayout, ViewPager.OnPageChangeListener {

    //constructor
    constructor(context: Context) :
            this(context, null)

    constructor(context: Context, attrs: AttributeSet?) :
            this(context, attrs!!, 0)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initialisation()
    }

    internal lateinit var iv_view_snap_center: ImageView
    internal lateinit var iv_view_snap_left: ImageView
    internal lateinit var iv_view_snap_right: ImageView
    internal lateinit var iv_view_snap_bottom: ImageView

    internal lateinit var view_snap_indicator: View

    internal lateinit var argbEvaluator: ArgbEvaluator
    internal var centerColor: Int = 0
    internal var sideColor: Int = 0

    internal var endViewTranslationX = 0
    internal var centerPadding = 0
    internal var centerTranslationY = 0

    fun SetUpWithViewPager(viewPager: ViewPager) {
        viewPager.addOnPageChangeListener(this)

        iv_view_snap_left.setOnClickListener {
            if (viewPager.currentItem != 0)
                viewPager.currentItem = 0
        }
        iv_view_snap_center.setOnClickListener {
            if (viewPager.currentItem != 1)
                viewPager.currentItem = 1
        }
        iv_view_snap_right.setOnClickListener {
            if (viewPager.currentItem != 2)
                viewPager.currentItem = 2
        }
    }

    private fun initialisation() {
        val view: View = LayoutInflater.from(context).inflate(R.layout.view_snap_tabs, this, true)

        //initialisation of child view
        iv_view_snap_center = view.findViewById(R.id.iv_view_snap_center)
        iv_view_snap_left = view.findViewById(R.id.iv_view_snap_left)
        iv_view_snap_right = view.findViewById(R.id.iv_view_snap_right)
        iv_view_snap_bottom = view.findViewById(R.id.iv_view_snap_bottom)
        view_snap_indicator = view.findViewById(R.id.view_snap_indicator)
        val fl_container: FrameLayout = view.findViewById(R.id.fl_container)

        //color
        centerColor = ContextCompat.getColor(context, R.color.colorWhite)
        sideColor = ContextCompat.getColor(context, R.color.colorGrey)

        //method to graduate color
        argbEvaluator = ArgbEvaluator()

        //a distance in dp
        centerPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80F, resources.displayMetrics).toInt()

        // to apply the code until the imageview take place to don't return 0
        iv_view_snap_bottom.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                //position x after transaction finish
                endViewTranslationX = ((iv_view_snap_bottom.x - iv_view_snap_left.x) - centerPadding).toInt()
                iv_view_snap_bottom.viewTreeObserver.removeOnGlobalLayoutListener(this)

                centerTranslationY = height - iv_view_snap_bottom.bottom
            }
        })

        //add padding, to display view on top of navigation bar
        val elevation = getNavigationBarHeight()
        fl_container.setPadding(0, 0, 0, elevation)
    }

    private fun getNavigationBarHeight(): Int {
        val id = resources.getIdentifier(
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    "navigation_bar_height"
                else
                    "navigation_bar_height_landscape", "dimen", "android")
        return if (id > 0) {
            resources.getDimensionPixelSize(id)
        } else 0
    }


    //to apply changes while scrolling to have the nice color changes
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (position == 0) {
            setColor(1 - positionOffset)
            moveViews(1 - positionOffset)
            view_snap_indicator.translationX = ((positionOffset - 1) * centerPadding)
            moveAndScaleCenter(1 - positionOffset)

        } else if (position == 1) {
            setColor(positionOffset)
            moveViews(positionOffset)
            moveAndScaleCenter(positionOffset)
            view_snap_indicator.translationX = (positionOffset * centerPadding)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageSelected(position: Int) {

    }

    private fun setColor(fractionFromCenter: Float) {
        val color: Int = argbEvaluator.evaluate(fractionFromCenter, centerColor, sideColor) as Int

        iv_view_snap_center.setColorFilter(color)
        iv_view_snap_right.setColorFilter(color)
        iv_view_snap_left.setColorFilter(color)
    }

    private fun moveViews(fractionFromCenter: Float) {
        iv_view_snap_left.translationX = fractionFromCenter * endViewTranslationX
        iv_view_snap_right.translationX = -fractionFromCenter * endViewTranslationX

        view_snap_indicator.alpha = fractionFromCenter
        view_snap_indicator.scaleX = fractionFromCenter
    }

    private fun moveAndScaleCenter(fractionFromCenter: Float) {
        val scale: Float = .7f + ((1 - fractionFromCenter) * .3f)
        iv_view_snap_center.scaleX = scale
        iv_view_snap_center.scaleY = scale

        val translation = fractionFromCenter * centerTranslationY
        iv_view_snap_center.translationY = translation
        iv_view_snap_bottom.translationY = translation

        iv_view_snap_bottom.alpha = (1 - fractionFromCenter)
    }
}