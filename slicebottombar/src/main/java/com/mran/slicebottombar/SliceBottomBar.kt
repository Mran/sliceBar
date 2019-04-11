package com.mran.slicebottombar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SliceBottomBar : ViewGroup {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
        mAttributeSet = attrs

    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)

    }

    private var mContext: Context? = null
    private var mAttributeSet: AttributeSet? = null
    //左右的padding
    private var padingLR = 50f
    //上下的pading
    private var padingTB = 20f
    //默认item内部高度
    private var defaultHeight = 140f
    //view的宽度
    private var itemWidth = 0f
    //view的数量
    private var itemSize = 0

    private val paint = Paint()
    //选中状态的起始位置
    private var selectRectX = padingLR

    private var lastSelectRectX = padingLR

    //选中状态的Index
    private var lastSelectIndex = 0
    private var newSelectIndex = 0

    //圆角弧度
    private var radius = 30f

    private var valueAnimator = ValueAnimator()
    //item背景颜色
    private var itemBackGroundColor = 0xa3b18f
    //Item的文字大小
    private var textSize = 40.0f
    //文字的颜色
    private var textColor = 0xffffff


    private fun init(context: Context, attrs: AttributeSet? = null) {
        setWillNotDraw(false)/*设置false之后才能调用ondraw()*/
        mAttributeSet = attrs
        mContext = context
        getAttrs()

        paint.color=itemBackGroundColor
        valueAnimator.duration = 200L
        valueAnimator.setFloatValues(0f, 1f)
        valueAnimator.removeAllUpdateListeners()

        valueAnimator.addUpdateListener(updateListener)
        valueAnimator.addListener(animatorListener)

    }


    private fun getAttrs() {
        val typedArray = mContext!!.obtainStyledAttributes(mAttributeSet, R.styleable.sliceBottomBar)


        itemBackGroundColor = typedArray.getColor(R.styleable.sliceBottomBar_itemBackgroundColor, 0xa3b18f)
        textSize = typedArray.getDimension(R.styleable.sliceBottomBar_textSize, 25.0f)
        textColor = typedArray.getColor(R.styleable.sliceBottomBar_textColor, 0xffffff)
        defaultHeight = typedArray.getDimension(R.styleable.sliceBottomBar_itemHeight, 120.0f)
        radius=typedArray.getDimension(R.styleable.sliceBottomBar_itemRadius, 50.0f)
        Log.d("AnimationBottomBar", "getAttrs:textSize $textSize")
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        for (i in 0 until childCount) {
            if (getChildAt(i) !is SliceItemView) {
                throw Throwable("Only SliceItemView")
            }
        }
    }

    fun addItem(icon: Int, name: String): SliceBottomBar {

        var sliceItemView = SliceItemView(context)
        sliceItemView.setIcon(icon)
        sliceItemView.setName(name)
        sliceItemView.setTextSize(textSize)
        addView(sliceItemView)
        itemSize++
        if (itemSize == 1) {
            sliceItemView.move()

        }
        return this
    }

    fun setItemTextSize(textSize: Float) {
        for (i in 0 until childCount) {
            (getChildAt(i) as SliceItemView).setTextSize(textSize)
        }
        invalidate()
    }

    fun setItemColor(color: String) {
        paint.color = Color.parseColor(color)
        invalidate()
    }

    fun setRadiu(radius: Float): SliceBottomBar {
        this.radius = radius
        return this
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)

        padingTB = max(paddingTop.toFloat(), paddingBottom.toFloat())
        padingLR = max(paddingLeft.toFloat(), paddingRight.toFloat())
        selectRectX = padingLR
        setMeasuredDimension(widthSpecSize, (defaultHeight + 2 * padingTB).toInt())
        if (itemSize != 0) {
            itemWidth = (widthSpecSize - 2 * padingLR) / itemSize
        } else {
            itemWidth = (widthSpecSize - 2 * padingLR)
        }
        for (i in 0 until itemSize) {
            var view = getChildAt(i)
            var childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(itemWidth.toInt(), MeasureSpec.EXACTLY)
            var childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(defaultHeight.toInt(), MeasureSpec.EXACTLY)
            view.measure(childWidthMeasureSpec, childHeightMeasureSpec)
        }

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
//        super.onLayout(changed, l, t, r, b)
        for (i in 0 until itemSize) {
            var view = getChildAt(i)
            view.layout(
                (padingLR + i * itemWidth).toInt(), padingTB.toInt(), (padingLR + (i + 1) * itemWidth).toInt(),
                (padingTB + defaultHeight).toInt()
            )
        }
    }

    override fun onDraw(canvas: Canvas) {

        canvas.drawRoundRect(
            selectRectX,
            padingTB,
            selectRectX + itemWidth,
            padingTB + defaultHeight,
            radius,
            radius,
            paint
        )
        super.onDraw(canvas)
    }

    var lastDonwTime = 0L
    var lastDonwX = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                lastDonwTime = System.currentTimeMillis()
                lastDonwX = event.x.toInt()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (abs(event.x - lastDonwX) < 10) {
                    for (i in 0 until itemSize) {
                        var view = getChildAt(i) as SliceItemView
                        if (lastDonwX > padingLR + i * itemWidth && lastDonwX < padingLR + (i + 1) * itemWidth && i != newSelectIndex) {
                            lastSelectIndex = newSelectIndex
                            newSelectIndex = i
                            view.move()
                            moveRect()
                        } else {
                            if (view.state == SliceItemView.State.MOVE_TO_NORMAL || view.state == SliceItemView.State.MOVE_TO_SELECT) {
                                view.calcelMove()
                            } else if (view.state == SliceItemView.State.SELECT) {
                                view.move()
                            }
                        }
                    }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    var updateListener = ValueAnimator.AnimatorUpdateListener {
        var value = it.animatedValue as Float
        selectRectX = lastSelectRectX + (padingLR + newSelectIndex * itemWidth - lastSelectRectX) * value
        invalidate()
    }
    var animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {

        }

        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {

            lastSelectRectX = selectRectX
            if (onItemSelectListener != null) {
                onItemSelectListener!!.onSelected(newSelectIndex)
            }

        }

        override fun onAnimationCancel(animation: Animator?) {


        }
    }

    fun moveRect() {
        if (valueAnimator.isRunning) {
            valueAnimator.cancel()
        }
        valueAnimator.start()

    }

    var onItemSelectListener: OnItemSelectListener? = null


    interface OnItemSelectListener {
        fun onSelected(index: Int)
    }
}
