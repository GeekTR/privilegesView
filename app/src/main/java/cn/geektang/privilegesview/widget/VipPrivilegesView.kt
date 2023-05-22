package cn.geektang.privilegesview.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import cn.geektang.privilegesview.utils.dp

class VipPrivilegesView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FDF4E2")
    }
    private val shader = createShader()
    private val shaderPath = Path()
    private val rightPath = Path()
    private val pathEffect = CornerPathEffect(1.dp)

    init {
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resetShaderPath(h)
        resetRightPath(w, h)
    }

    private fun resetRightPath(viewWidth: Int, viewHeight: Int) {
        val radius = 16.dp
        val leftRectWidth = 100.dp

        rightPath.reset()
        rightPath.moveTo(leftRectWidth, 0f)
        rightPath.lineTo(viewWidth.toFloat() - radius, 0f)
        // 右上圆角
        rightPath.arcTo(
            viewWidth.toFloat() - radius,
            0f,
            viewWidth.toFloat(),
            radius,
            -90f,
            90f,
            false
        )
        rightPath.lineTo(viewWidth.toFloat(), viewHeight.toFloat() - radius)
        // 右下圆角
        rightPath.arcTo(
            viewWidth.toFloat() - radius,
            viewHeight.toFloat() -radius,
            viewWidth.toFloat(),
            viewHeight.toFloat(),
            0f,
            90f,
            false
        )
        rightPath.lineTo(100.dp, viewHeight.toFloat())
        rightPath.close()
    }

    private fun resetShaderPath(viewHeight: Int) {
        val triangleWidth = 6.dp
        val triangleHeight = 16.dp
        val radius = 16.dp
        val leftRectWidth = 100.dp

        shaderPath.reset()
        shaderPath.moveTo(radius, 0f)
        // 最上面的那条线
        shaderPath.lineTo(leftRectWidth, 0f)
        // 最后边的线+三角形
        shaderPath.lineTo(leftRectWidth, (viewHeight - triangleHeight) / 2f)
        shaderPath.lineTo(leftRectWidth + triangleWidth, viewHeight / 2f)
        shaderPath.lineTo(leftRectWidth, (viewHeight + triangleHeight) / 2f)

        // 剩下的线+圆角
        shaderPath.lineTo(leftRectWidth, viewHeight.toFloat())
        shaderPath.lineTo(radius, viewHeight.toFloat())
        shaderPath.arcTo(0f, viewHeight - radius, radius, viewHeight.toFloat(), 90f, 90f, false)
        shaderPath.lineTo(0f, radius)
        shaderPath.arcTo(0f, 0f, radius, radius, 180f, 90f, false)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPath(rightPath, paint)

        paint.shader = shader
        paint.pathEffect = pathEffect
        canvas.drawPath(shaderPath, paint)
        paint.pathEffect = null
        paint.shader = null

    }

    private fun createShader(): Shader {
        val startColor = Color.parseColor("#FEDD83")
        val endColor = Color.parseColor("#F2CA5C")
        return LinearGradient(
            0f,
            0f,
            106.dp,
            0f,
            startColor,
            endColor,
            Shader.TileMode.CLAMP
        )
    }
}