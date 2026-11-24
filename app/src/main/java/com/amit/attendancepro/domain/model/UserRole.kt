package com.amit.attendancepro.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Store
import androidx.compose.ui.graphics.vector.ImageVector

enum class UserRole(
    val displayName: String,
    val description: String,
    val icon: ImageVector
) {
    SUPER_ADMIN("Super Admin", "Full system access", Icons.Default.AdminPanelSettings),
    STORE_ADMIN("Store Admin", "Manage your store", Icons.Default.Store),
    MANAGER("Manager", "Manage your team", Icons.Default.Badge),
    EMPLOYEE("Employee", "Mark attendance", Icons.Default.Person)
}
