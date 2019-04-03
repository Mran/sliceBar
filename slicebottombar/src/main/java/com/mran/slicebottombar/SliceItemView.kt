package com.mran.slicebottombar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Transformation
import kotlin.math.abs

class SliceItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {


    val paint = Paint()
    var paintColor = 0xffff62
    var paintSize = 40.0f
    val paintSelectColor = 0x000000
    var iconResource = 0
    var text = "123123"
    //中心点坐标
    var centerX = 0
    var centerY: Int = 0

    //文字的位置
    var cX = 0f
    var cY = 0f
    //当前的状态
    var state = State.NORMAL
    //icon的运动位置
    var iconX = 0.0f
    var iconY = 0.0f

    //icon的起始位置
    var iconStartX = 0.0f
    //icon的终止位置
    var iconEndX = 0.0f

    var iconBitmap: Bitmap? = null
    var textHeight = 0
    var iconHeight = 0
    var iconWidth = 0
    var textWidth = 0
    var textAlpha = 0
    var valueAnimator = ValueAnimator()
    var currentValue = 0f
    var targetValue = 1f
    var isCancel = false

    //运动状态
    enum class State {
        MOVE_TO_SELECT,
        SELECT,
        NORMAL,
        MOVE_TO_NORMAL
    }

    var updateListener = ValueAnimator.AnimatorUpdateListener {
        currentValue = it.animatedValue as Float
//        Log.d("applyTransformation", "${currentValue}")

        iconX = iconStartX - abs((iconStartX - iconEndX)) * (currentValue)
        textAlpha = (currentValue * 255).toInt()
        invalidate()
    }
    var animatorListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
//            Log.d("onAnimationStart", "onAnimationStart")

            isCancel = false
            when (state) {
                State.NORMAL -> {
                    state = State.MOVE_TO_SELECT
                }
                State.SELECT -> {
                    state = State.MOVE_TO_NORMAL
                }

            }
        }

        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {

            when (state) {
                State.MOVE_TO_NORMAL -> {
                    state = State.NORMAL
                    targetValue = 1f
                    currentValue = 0f
//                    Log.d("onAnimationEnd", "MOVE_TO_NORMAL")
                }
                State.MOVE_TO_SELECT -> {
                    state = State.SELECT
                    targetValue = 0f
                    currentValue = 1f
//                    Log.d("onAnimationEnd", "MOVE_TO_SELECT")
                }

            }
        }

        override fun onAnimationCancel(animation: Animator?) {


        }
    }

    init {
        paint.isAntiAlias = true
        paint.color = Color.WHITE
        paint.textSize = paintSize
        setLayerType(LAYER_TYPE_HARDWARE, paint)

        valueAnimator.duration = 200L
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener(updateListener)
        valueAnimator.addListener(animatorListener)
    }

    fun setIcon(icon: Int) {
        iconResource = icon
        iconBitmap = BitmapFactory.decodeResource(context!!.resources, iconResource)
        invalidate()
    }

    fun setName(name: String) {
        this.text = name
        invalidate()
    }

    fun setTextColor(color: Int) {
        this.paintColor = color
        paint.color = color
        invalidate()
    }

    fun setTextSize(size: Float) {
        this.paintSize = size
        paint.textSize = size
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (iconBitmap != null)
            canvas.drawBitmap(iconBitmap, iconX, iconY, paint)
        val lastAlpha = paint.alpha
        paint.alpha = textAlpha
        canvas.drawText(text, cX, cY, paint)
        paint.alpha = lastAlpha
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
        //中心点坐标
        centerX = widthSpecSize / 2
        centerY = heightSpecSize / 2
        //设置宽高
        setMeasuredDimension(widthSpecSize, heightSpecSize)
        //获取文字的大小
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)
        textHeight = rect.height()
        textWidth = rect.width()
        //获取文字的坐标
        cX = centerX.toFloat()
        cY = (heightSpecSize / 2).toFloat() + textHeight / 2
        if (iconBitmap != null) {
            //获取图标的起始坐标
            iconStartX = (centerX - iconBitmap!!.width / 2).toFloat()
            //获取图标的终止坐标
            iconEndX = (centerX / 2 - iconBitmap!!.width / 2).toFloat()
            //初始化图标运动坐标
            iconX = iconStartX
            iconY = (centerY - iconBitmap!!.height / 2).toFloat()
        }

    }


    fun move() {
        if (valueAnimator.isRunning) {
            return
        }
        valueAnimator.cancel()
        valueAnimator.duration = 200L
        valueAnimator.setFloatValues(currentValue, targetValue)


        valueAnimator.start()
    }

    fun calcelMove() {
        valueAnimator.reverse()


    }
}
