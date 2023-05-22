package cn.geektang.privilegesview.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginEnd
import cn.geektang.privilegesview.R
import cn.geektang.privilegesview.utils.sp
import java.lang.Integer.max
import kotlin.math.roundToInt

class VipPrivilegesItemView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private lateinit var staticLayout: StaticLayout
    private var hasExtraLine = false
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 13.sp
    }
    var text: CharSequence = ""
        set(value) {
            if (field != value) {
                field = value
                requestLayout()
            }
        }

    init {
        setWillNotDraw(false)
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.VipPrivilegesItemView)
            try {
                if (ta.hasValue(R.styleable.VipPrivilegesItemView_android_text)) {
                    text = ta.getText(R.styleable.VipPrivilegesItemView_android_text)
                }
                if (ta.hasValue(R.styleable.VipPrivilegesItemView_android_textColor)) {
                    val textColor = ta.getColor(
                        R.styleable.VipPrivilegesItemView_android_textColor,
                        Color.BLACK
                    )
                    textPaint.color = textColor
                }

                if (ta.hasValue(R.styleable.VipPrivilegesItemView_android_textSize)) {
                    val textSize = ta.getDimensionPixelSize(
                        R.styleable.VipPrivilegesItemView_android_textSize,
                        13.sp.roundToInt()
                    )
                    textPaint.textSize = textSize.toFloat()
                }

            } finally {
                ta.recycle()
            }
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount > 1) {
            throw IllegalArgumentException("VipPrivilegesItemView can only have 1 children")
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        staticLayout = StaticLayout.Builder
            .obtain(text, 0, text.length, textPaint, width)
            .build()
        if (childCount > 0) {
            val lastLineWidth = staticLayout.getLineWidth(staticLayout.lineCount - 1)
            val childView = getChildAt(0)
            if (childView.visibility == View.GONE) {
                hasExtraLine = false
                setMeasuredDimension(
                    width + paddingStart + paddingEnd,
                    staticLayout.height + paddingTop + paddingBottom
                )
                return
            }

            measureChildWithMargins(childView, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val childWidth = childView.measuredWidth
            val childViewLp = childView.layoutParams as MarginLayoutParams
            if (lastLineWidth + childWidth + childViewLp.marginStart + childView.marginEnd > width) {
                hasExtraLine = true
                setMeasuredDimension(
                    width + paddingStart + paddingEnd,
                    staticLayout.height + paddingTop + paddingBottom + childView.measuredHeight
                            + childViewLp.topMargin + childViewLp.bottomMargin
                )
            } else {
                hasExtraLine = false
                setMeasuredDimension(
                    width + paddingStart + paddingEnd,
                    staticLayout.height + paddingTop + paddingBottom
                )
            }
        } else {
            hasExtraLine = false
            setMeasuredDimension(
                width + paddingStart + paddingEnd,
                staticLayout.height + paddingTop + paddingBottom
            )
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount <= 0 || getChildAt(0).visibility == View.GONE) {
            return
        }
        val childView = getChildAt(0)
        if (hasExtraLine) {
            // 有多余的一行，childView在StaticLayout的下面
            val childViewLp = childView.layoutParams as MarginLayoutParams
            childView.layout(
                paddingStart + childViewLp.marginStart,
                staticLayout.height + childViewLp.topMargin,
                childView.measuredWidth + paddingStart + childViewLp.marginStart,
                staticLayout.height + childView.measuredHeight + childViewLp.topMargin
            )
        } else {
            // 没有多余的一行，childView摆放到staticLayout的最后一行的最后
            val childViewLp = childView.layoutParams as MarginLayoutParams
            val childViewLeft = staticLayout.getLineWidth(staticLayout.lineCount - 1)
                .roundToInt() + paddingStart + childViewLp.marginStart

            val childViewTop =
                staticLayout.height - childView.measuredHeight + childViewLp.topMargin
            childView.layout(
                childViewLeft,
                childViewTop,
                childViewLeft + childView.measuredWidth,
                childViewTop + childView.measuredHeight
            )
        }
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onDraw(canvas: Canvas) {
        staticLayout.draw(canvas)
    }
}