package com.souqApp.domain.common.entity

enum class NotificationType(private val code: String) {
    GENERAL("1"),
    COUPON("2"),
    ORDER("3"),
    PRODUCT("4");

    companion object {
        fun findByCode(code: String?): NotificationType? {
            return entries.firstOrNull { it.code == code }
        }
    }
}


