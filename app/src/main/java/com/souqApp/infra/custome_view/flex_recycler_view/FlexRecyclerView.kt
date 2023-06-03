package com.souqApp.infra.custome_view.flex_recycler_view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.R


@Suppress("unused")
@SuppressLint("Recycle", "CustomViewStyleable")
class FlexRecyclerView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    private val recyclerView = RecyclerView(context)

    private val typedArray by lazy {
        context.obtainStyledAttributes(attrs, R.styleable.flexRecycler, 0, 0)
    }

    fun <T> setAdapter(adapter: FlexRecyclerAdapter<T>, layoutManager: RecyclerView.LayoutManager) {
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    init {
        recyclerView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        addView(recyclerView)

        val showEmptyState = typedArray.getBoolean(R.styleable.flexRecycler_showEmptyState, false)
        setupEmptyState(showEmptyState)

        if (isInEditMode) {
            setupPreviewAdapter()
        }
    }

    fun addItemDecoration(itemDecoration: RecyclerView.ItemDecoration) {
        recyclerView.addItemDecoration(itemDecoration)
    }

    fun setupEmptyState(showEmptyState: Boolean) {
        val emptyState = typedArray.getResourceId(R.styleable.flexRecycler_emptyState, 0)

        if (emptyState != 0 && showEmptyState) {
            recyclerView.isVisible = false
            val layout: View = LayoutInflater.from(context).inflate(emptyState, this, false)
            val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)

            val animation: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
             layout.startAnimation(animation)

            params.gravity = Gravity.CENTER
            layout.layoutParams = params

            addView(layout)
        }
    }


    private fun setupPreviewAdapter() {
        val listItem = typedArray.getResourceId(R.styleable.flexRecycler_listItem, 0)
        val itemCount = typedArray.getInt(R.styleable.flexRecycler_emptyState, 3)

        if (listItem != 0) {

            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): ViewHolder = ViewHolder(
                    LayoutInflater.from(parent.context).inflate(listItem, parent, false)
                )

                override fun getItemCount(): Int = itemCount

                override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = Unit

            }
        }
    }

}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
