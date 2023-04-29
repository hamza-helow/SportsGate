package com.souqApp.infra.utils

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.souqApp.infra.custome_view.LabelWithValueHorizontal
import com.souqApp.infra.extension.inVisible
import com.souqApp.infra.extension.isVisible
import kotlin.math.roundToInt

@SuppressLint("CheckResult")
@BindingAdapter(value = ["networkImage", "placeholder", "resizeImage"], requireAll = false)
fun ImageView.setImageUrl(url: String?, placeholder: Drawable? = null, resize: Boolean? = true) {
    if (url == null || url.isEmpty()) {
        setImageDrawable(placeholder)
        return
    }

    Glide.with(this)
        .load(url)
        .apply {
            if (resize != false)
                override(300, 300)
        }
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@BindingAdapter("horizontalPadding")
fun setHorizontalPadding(view: View, margin: Float) {
    view.setPadding(0, 0, margin.toInt(), 0)
}


@BindingAdapter("android:movementMethod")
fun enableMovementMethod(view: TextView, enable: Boolean?) {
    if (enable == true)
        view.movementMethod = LinkMovementMethod.getInstance()
}

@BindingAdapter("android:text")
fun setHtmlText(view: TextView, text: String?) {

    if (text == null) {
        return
    }

    view.text = HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY)
}

@BindingAdapter("layout_marginBottom")
fun setBottomMargin(view: View, bottomMargin: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin, layoutParams.topMargin,
        layoutParams.rightMargin, bottomMargin.roundToInt()
    )
    view.layoutParams = layoutParams
}

@BindingAdapter("isVisible")
fun View.setIsVisible(isVisible: Boolean) {
    this.isVisible(isVisible)
}

@BindingAdapter("inVisible")
fun setInVisible(view: View, inVisible: Boolean) {
    view.inVisible(inVisible)
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("isDiscountPrice")
fun TextView.discountTextView(isDiscountPrice: Boolean) {
    if (isDiscountPrice) {
        paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        setTextColor(Color.RED)
        typeface = Typeface.DEFAULT
    } else {
        setTextColor(Color.BLACK)
        typeface = Typeface.DEFAULT_BOLD
        paintFlags = 0
    }
}

@BindingAdapter("value")
fun LabelWithValueHorizontal.setValue(value: String?) {
    txtValue.text = value.orEmpty().ifEmpty { "-" }
}