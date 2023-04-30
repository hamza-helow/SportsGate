package com.souqApp.presentation.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _cartQtyLiveData: MutableLiveData<Int> = MutableLiveData()
    val cartQtyLiveData: LiveData<Int> get() = _cartQtyLiveData

    fun setQty(qty: Int) {
        _cartQtyLiveData.value = qty
    }
}