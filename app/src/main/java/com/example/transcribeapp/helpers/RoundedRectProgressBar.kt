package com.example.transcribeapp.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.transcribeapp.R

class RoundedRectProgressBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var progress = 0
    private val rects = 10 // Number of rounded rectangles
    private val rectWidth: Float = 23f
    private val rectHeight: Float = 40f
    private val spacing: Float = 7f

    init {
        paint.color = context.getColor(R.color.progress_Dark)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            for (i in 0 until rects) {
                if (i < (progress * rects) / 100) {
                    paint.color = context.getColor(R.color.light_blue) // Filled rectangles
                } else {
                    paint.color =context.getColor(R.color.progress_Dark)// Empty rectangles
                }
                val left = (i * (rectWidth + spacing))
                val top = 0f
                it.drawRoundRect(left, top, left + rectWidth, top + rectHeight, 10f, 10f, paint)
            }
        }
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate()
    }
}
