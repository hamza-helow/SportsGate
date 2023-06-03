package com.souqApp.domain.main.home

data class CheckUpdateEntity(
    val forceUpdate: Boolean,
    val latestVersion: String,
    val newVersion: Boolean,
    val storeUrl: String
)