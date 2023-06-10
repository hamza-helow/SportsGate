package com.souqApp.infra.custome_view.flex_recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.LinkedList
import java.util.Queue

@Suppress("unused", "MemberVisibilityCanBePrivate")
abstract class FlexRecyclerAdapter<MODEL>( list: List<MODEL> = mutableListOf()) :

    RecyclerView.Adapter<FlexRecyclerAdapter.FlexViewHolder>() {

    val dataList: MutableList<MODEL> = list.toMutableList()
    private val queue: Queue<List<MODEL>> = LinkedList()
    private var paginationListener: PaginationListener? = null
    private var currentPage: Int = 0
    private val pageNumber: Int get() = ++currentPage

    fun setPaginationListener(paginationListener: PaginationListener) {
        this.paginationListener = paginationListener
        this.currentPage = paginationListener.startPage
    }

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexViewHolder {
        return onCreateViewHolder(LayoutInflater.from(parent.context), parent, viewType)
    }

    abstract fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup,
        viewType: Int
    ): FlexViewHolder

    final override fun onBindViewHolder(holder: FlexViewHolder, position: Int) {
        setupViewHolder(holder, position, dataList[position])
    }

    /**
     * When you need setup View Holder , bind data ,modify views , ....
     * */
    abstract fun setupViewHolder(holder: FlexViewHolder, position: Int, item: MODEL)

    final override fun getItemCount(): Int = dataList.size

    final override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val canScroll =
                    !recyclerView.canScrollVertically(1) || !recyclerView.canScrollHorizontally(1)

                if (canScroll && newState == RecyclerView.SCROLL_STATE_IDLE && queue.isNotEmpty()) {
                    addList(queue.poll().orEmpty(), enablePaging = false)
                } else {
                    setupPagination(recyclerView, newState)
                }
            }
        })
    }


    private fun setupPagination(recyclerView: RecyclerView, newState: Int) {

        if (newState == SCROLL_STATE_IDLE && paginationListener != null) {

            if (paginationListener?.isLastPage == true) return

            val layoutManager = recyclerView.layoutManager
            val visibleItemCount: Int = layoutManager?.childCount ?: 0
            val totalItemCount: Int = layoutManager?.itemCount ?: 0
            var firstVisibleItemPosition = 0

            if (layoutManager is GridLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

            } else if (layoutManager is LinearLayoutManager) {
                firstVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            }

            if (firstVisibleItemPosition + visibleItemCount >= totalItemCount) {
                paginationListener?.loadMore(pageNumber)
            }
        }
    }

    /**
     * Add an item at the end of the list
     * */
    fun addItem(model: MODEL) {
        dataList.add(model)
        notifyItemInserted(dataList.size)
    }

    /**
     * Add item in specific position
     * */
    fun addItem(model: MODEL, position: Int) {
        dataList.add(position, model)
        notifyItemInserted(position)
    }

    /**
     * Remove item by position
     * */
    fun removeItem(position: Int) {
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size)
    }

    /**
     * Remove item by type
     * */
    fun removeItem(item: MODEL) {
        val position = dataList.indexOf(item)
        dataList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, dataList.size)
    }

    /**
     * Remove all items from adapter
     * */
    @SuppressLint("NotifyDataSetChanged")
    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
        System.gc()
    }

    /**
     * Add new list on current list (current list + new list )
     * @param dataList : new list
     * */
    fun addList(dataList: List<MODEL>, enablePaging: Boolean = true) {

        if (enablePaging) {
            val lists = dataList.chunked(20)
            lists.forEach { queue.add(it) }

            if (queue.isNotEmpty()) {
                queue.poll()?.let {
                    this.dataList.addAll(it.toList())
                    notifyItemRangeChanged(this.dataList.size, dataList.size)
                }
            }
        } else {
            this.dataList.addAll(dataList)
            notifyItemRangeChanged(this.dataList.size, dataList.size)
        }
    }

    /**
     * Clear list and add new list
     * @param dataList replacement list
     * */
    @SuppressLint("NotifyDataSetChanged")
    open fun replaceList(dataList: List<MODEL>?) {
        this.dataList.clear()
        dataList?.let { addList(it) }
        notifyDataSetChanged()
    }


    open class FlexViewHolder(open val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(idVar: Int, item: Any?) {
            binding.setVariable(idVar, item)
            binding.executePendingBindings()
        }
    }
}
