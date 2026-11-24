package com.amit.attendancepro.domain.model

data class Store(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val adminUid: String = "",
    val adminEmail: String = "",
    val qrToken: String = "",
    val createdAt: Long = 0L
)
