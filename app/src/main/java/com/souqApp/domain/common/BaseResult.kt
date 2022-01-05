package com.souqApp.domain.common

sealed class BaseResult<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : BaseResult<T, Nothing>()
    data class Errors<U : Any>(val error: U) : BaseResult<Nothing, U>()
}