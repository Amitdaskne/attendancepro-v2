package com.amit.attendancepro.domain.model

data class UserProfile(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val role: String = UserRole.EMPLOYEE.name,
    val createdAt: Long = 0L
)
