package com.example.remotecontroller

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import com.example.remotecontroller.utils.FanSpeed
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private const val RADIUS_OFFSET_LABEL = 65
private const val RADIUS_OFFSET_INDICATION = -35
private const val CIRCLE_PADDING = 20

class FanControllerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius = 0.0f
    private var fanSpeed = FanSpeed.OFF
    private var isTextMode = false
    private val pointPosition: PointF = PointF(0.0f, 0.0f)

    private var lowColor = 0
    private var mediumColor = 0
    private var highColor = 0
    private var highestColor = 0

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.FanView) {
            lowColor = getColor(R.styleable.FanView_color1, 0)
            mediumColor = getColor(R.styleable.FanView_color2, 0)
            highColor = getColor(R.styleable.FanView_color3, 0)
            highestColor = getColor(R.styleable.FanView_color4, 0)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        toggleFanSpeed()
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = (min(w, h) / 2.0 * 0.8).toFloat()
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 25.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    private fun PointF.computeXYForSpeed(position: FanSpeed, radius: Float) {
        val startAngle = Math.PI * (9 / 8.0)
        val angle = startAngle + position.ordinal * (Math.PI / 4)
        x = (radius * cos(angle)).toFloat() + width / 2
        y = (radius * sin(angle)).toFloat() + width / 2
    }

    override fun onDraw(canvas: Canvas) {
        val paddedRadius = radius - CIRCLE_PADDING

        paint.color = when (fanSpeed) {
            FanSpeed.OFF -> Color.GRAY
            FanSpeed.LOW -> lowColor
            FanSpeed.MEDIUM -> mediumColor
            FanSpeed.HIGH -> highColor
            FanSpeed.HIGHEST -> highestColor
        }

        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), paddedRadius, paint)

        val markerRadius = paddedRadius + RADIUS_OFFSET_INDICATION
        pointPosition.computeXYForSpeed(fanSpeed, markerRadius)
        paint.color = Color.BLACK
        canvas.drawCircle(pointPosition.x, pointPosition.y, paddedRadius / 12, paint)

        val labelRadius = paddedRadius + RADIUS_OFFSET_LABEL
        for (i in FanSpeed.values()) {
            pointPosition.computeXYForSpeed(i, labelRadius)
            val label = if (isTextMode) {
                resources.getString(i.labelWord)
            } else {
                resources.getString(i.labelNumber)
            }
            canvas.drawText(label, pointPosition.x, pointPosition.y, paint)
        }

    }

    fun setTextMode(enabled: Boolean) {
        isTextMode = enabled
        invalidate()
    }

    fun toggleFanSpeed() {
        fanSpeed = fanSpeed.next()
        contentDescription = resources.getString(fanSpeed.labelNumber)

        invalidate()
    }

    fun getFanSpeed(): FanSpeed {
        return fanSpeed
    }
}
