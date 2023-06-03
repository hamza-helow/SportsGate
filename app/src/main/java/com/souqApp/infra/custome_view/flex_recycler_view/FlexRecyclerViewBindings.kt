package com.souqApp.infra.custome_view.flex_recycler_view

import androidx.databinding.BindingAdapter


@BindingAdapter("showEmptyState")
fun FlexRecyclerView.showEmptyState(show: Boolean) {
    setupEmptyState(show)
}