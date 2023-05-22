package cn.geektang.privilegesview.widget

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class NoScrollRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val gestureDetector: GestureDetector =
        GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                return isEnabled && isClickable
            }

            override fun onSingleTapUp(e: MotionEvent): Boolean {
                if (isEnabled && isClickable) {
                    performClick()
                    return true
                }
                return false
            }
        })

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return false
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(e)
    }
}