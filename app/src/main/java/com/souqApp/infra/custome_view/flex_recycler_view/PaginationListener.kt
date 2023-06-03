package com.souqApp.infra.custome_view.flex_recycler_view

interface PaginationListener {
    val startPage: Int
    val isLastPage: Boolean

    fun loadMore(pageNumber: Int)
}
