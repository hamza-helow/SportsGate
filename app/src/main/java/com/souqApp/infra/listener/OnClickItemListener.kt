package com.souqApp.infra.listener


interface OnClickItemListener<T> {

    fun onClickItem(item: T?) {}

    fun onClickItem(item: T?, position: Int) {}

}