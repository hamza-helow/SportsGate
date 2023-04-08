package com.souqApp.infra.extension


public fun <T> List<T>.secondOrNull(): T? {
    return this.getOrNull(1)
}