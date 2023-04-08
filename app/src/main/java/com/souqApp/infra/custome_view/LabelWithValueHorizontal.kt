package com.souqApp.infra.custome_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.souqApp.R

@SuppressLint("Recycle", "CustomViewStyleable")
class LabelWithValueHorizontal @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val txtLabel: TextView by lazy { findViewById(R.id.txt_label) }
    val txtValue: TextView by lazy { findViewById(R.id.txt_value) }
    val dividerView: View by lazy { findViewById(R.id.divider) }

    private val typedArray by lazy {
        context.obtainStyledAttributes(attrs, R.styleable.labelWithValue, 0, 0)
    }

    init {
        inflate(context, R.layout.custom_view_label_with_value_horizontal, this)
        val label = typedArray.getString(R.styleable.labelWithValue_label).orEmpty()
        val value = typedArray.getString(R.styleable.labelWithValue_value).orEmpty()
        val boldValue = typedArray.getBoolean(R.styleable.labelWithValue_boldValue, false)
        val divider = typedArray.getBoolean(R.styleable.labelWithValue_hasDivider, true)

        dividerView.isVisible = divider
        txtLabel.text = label
        txtValue.text = value

        if (boldValue) {
            txtValue.typeface = Typeface.DEFAULT_BOLD
        }
    }

}

@BindingAdapter("app:value")
fun LabelWithValueHorizontal.setValue(value: String?) {
    txtValue.text = value.orEmpty().ifEmpty { "-" }
}