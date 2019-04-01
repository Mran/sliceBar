package com.mran.slicebottombar

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs

class SliceItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {


    val paint = Paint()
    val paintColor = 0xffff62
    val paintSize = 40.0f
    val paintSelectColor = 0x000000
    var iconResource = R.drawable.home
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
    var currentValue = 0
    var targetValue = 100
    var isCancel = false

    enum class State {
        MOVE_TO_SELECT,
        SELECT,
        NORMAL,
        MOVE_TO_NORMAL
    }

    init {
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.textSize = paintSize
        iconBitmap = BitmapFactory.decodeResource(context!!.resources, iconResource)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            canvas.drawBitmap(iconBitmap!!, iconX, iconY, paint)
            val lastAlpha = paint.alpha
            paint.alpha = textAlpha
            canvas.drawText(text, cX, cY, paint)
            paint.alpha = lastAlpha
        }
    }

    //画出正常状态
    private fun drawNormal(canvas: Canvas) {
        canvas.drawText(text, cX, cY, paint)
        canvas.drawBitmap(iconBitmap!!, iconX, iconY, paint)
    }

    //画出选择状态
    private fun drawSelect(canvas: Canvas) {
        canvas.drawText(text, cX, 0f, paint)
    }

    //画出从正常到选择的中间态
    private fun drawMoveToSelect(canvas: Canvas) {
        canvas.drawText(text, cX, 0f, paint)
    }

    //画出从选择到正常的中间态
    private fun drawMoveToNormal(canvas: Canvas) {
        canvas.drawText(text, cX, 0f, paint)
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
        //获取图标的起始坐标
        iconStartX = (centerX - iconBitmap!!.width / 2).toFloat()
        //获取图标的终止坐标
        iconEndX = (centerX / 2 - iconBitmap!!.width / 2).toFloat()

        //初始化图标运动坐标
        iconX = iconStartX
        iconY = (centerY - iconBitmap!!.height / 2).toFloat()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun move() {
        valueAnimator.cancel()
        valueAnimator.duration = (3000 * (abs(targetValue - currentValue)) / 100).toLong()
        valueAnimator.setIntValues(currentValue, targetValue)
        valueAnimator.addUpdateListener {
            println(it.animatedValue as Int)
            currentValue = it.animatedValue as Int
        }

        valueAnimator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
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
                if (isCancel) {

                    return
                }
                when (state) {
                    State.MOVE_TO_NORMAL -> {
                        state = State.NORMAL
                        targetValue = 100
                        currentValue = 0
                        Log.d("onAnimationEnd", "MOVE_TO_NORMAL")

                    }
                    State.MOVE_TO_SELECT -> {
                        state = State.SELECT
                        targetValue = 0
                        currentValue = 100
                        Log.d("onAnimationEnd", "MOVE_TO_SELECT")

                    }

                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                if (isCancel)
                    return
                isCancel = true
                if (state == State.MOVE_TO_NORMAL) {
                    state = State.MOVE_TO_SELECT
                    targetValue = 100
                    Log.d("onAnimationCancel", "MOVE_TO_NORMAL")
                    move()
                    return
                } else if (state == State.MOVE_TO_SELECT) {
                    state = State.MOVE_TO_NORMAL
                    targetValue = 0
                    Log.d("onAnimationCancel", "MOVE_TO_SELECT")

                    move()
                    return


                }


            }
        })
        valueAnimator.start()
    }

    fun calcelMove() {
        valueAnimator.cancel()
//        valueAnimator.setDuration(3000)
//        valueAnimator.setIntValues(currentValue, 0)
//        valueAnimator.addUpdateListener {
//            println(it.animatedValue as Int)
//            currentValue = it.animatedValue as Int
//        }
//        valueAnimator.start()
    }
}
