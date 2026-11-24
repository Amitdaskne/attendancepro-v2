package com.amit.attendancepro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.amit.attendancepro.domain.model.UserRole
import com.amit.attendancepro.ui.auth.AuthState
import com.amit.attendancepro.ui.auth.AuthViewModel
import com.amit.attendancepro.ui.auth.LoginScreen
import com.amit.attendancepro.ui.auth.SignupScreen
import com.amit.attendancepro.ui.screens.employee.EmployeeDashboard
import com.amit.attendancepro.ui.screens.manager.ManagerDashboard
import com.amit.attendancepro.ui.screens.storeadmin.StoreAdminDashboard
import com.amit.attendancepro.ui.screens.superadmin.SuperAdminDashboard
import com.amit.attendancepro.ui.stores.CreateStoreScreen
import com.amit.attendancepro.ui.stores.StoreListScreen
import com.amit.attendancepro.ui.stores.StoreQRScreen

object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val EMPLOYEE = "employee"
    const val MANAGER = "manager"
    const val STORE_ADMIN = "store_admin"
    const val SUPER_ADMIN = "super_admin"
    const val STORE_CREATE = "store_create"
    const val STORE_LIST = "store_list"
    const val STORE_QR = "store_qr"
}

@Composable
fun AppNavigation(vm: AuthViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) { vm.checkAuth() }

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            val role = (state as AuthState.Authenticated).profile.role
            val route = when (UserRole.valueOf(role)) {
                UserRole.EMPLOYEE -> Routes.EMPLOYEE
                UserRole.MANAGER -> Routes.MANAGER
                UserRole.STORE_ADMIN -> Routes.STORE_ADMIN
                UserRole.SUPER_ADMIN -> Routes.SUPER_ADMIN
            }
            if (navController.currentDestination?.route != route) {
                navController.navigate(route) { popUpTo(0) }
            }
        }
    }

    NavHost(navController, startDestination = Routes.LOGIN) {
        composable(Routes.LOGIN) {
            LoginScreen(
                state = state,
                onLogin = { e, p -> vm.login(e, p) },
                onGoToSignup = { vm.clearError(); navController.navigate(Routes.SIGNUP) }
            )
        }
        composable(Routes.SIGNUP) {
            SignupScreen(
                state = state,
                onSignup = { n, e, p, r -> vm.signup(n, e, p, r) },
                onGoToLogin = { vm.clearError(); navController.popBackStack() }
            )
        }
        composable(Routes.EMPLOYEE) {
            EmployeeDashboard(onBack = { vm.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } })
        }
        composable(Routes.MANAGER) {
            ManagerDashboard(onBack = { vm.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } })
        }
        composable(Routes.STORE_ADMIN) {
            StoreAdminDashboard(
                onBack = { vm.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                onCreateStore = { navController.navigate(Routes.STORE_CREATE) },
                onMyStore = {
                    navController.navigate(Routes.STORE_LIST) { }
                }
            )
        }
        composable(Routes.SUPER_ADMIN) {
            SuperAdminDashboard(
                onBack = { vm.logout(); navController.navigate(Routes.LOGIN) { popUpTo(0) } },
                onManageStores = { navController.navigate(Routes.STORE_LIST) }
            )
        }
        composable(Routes.STORE_CREATE) {
            CreateStoreScreen(
                onBack = { navController.popBackStack() },
                onCreated = { storeId ->
                    navController.navigate("${Routes.STORE_QR}/$storeId") {
                        popUpTo(Routes.STORE_CREATE) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.STORE_LIST) {
            StoreListScreen(
                onBack = { navController.popBackStack() },
                onStoreClick = { storeId -> navController.navigate("${Routes.STORE_QR}/$storeId") },
                onCreateStore = { navController.navigate(Routes.STORE_CREATE) }
            )
        }
        composable(
            route = "${Routes.STORE_QR}/{storeId}",
            arguments = listOf(navArgument("storeId") { type = NavType.StringType })
        ) { entry ->
            val storeId = entry.arguments?.getString("storeId") ?: ""
            StoreQRScreen(storeId = storeId, onBack = { navController.popBackStack() })
        }
    }
}
