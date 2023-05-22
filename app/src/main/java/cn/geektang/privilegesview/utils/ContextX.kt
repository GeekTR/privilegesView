package cn.geektang.privilegesview.utils

import android.util.TypedValue
import android.view.View

context (View)
val Float.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        context.resources.displayMetrics
    )

context (View)
val Int.dp
    get() = this.toFloat().dp

context (View)
val Float.sp
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, this, context.resources.displayMetrics)

context (View)
val Int.sp
    get() = this.toFloat().sp