package com.baccaro.kmp

import CategoryDetailScreen
import ClientScaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.baccaro.kmp.presentation.auth.LoginScreen
import com.baccaro.kmp.presentation.auth.RegisterScreen
import com.baccaro.kmp.presentation.auth.RegisterWorkerScreen
import com.baccaro.kmp.presentation.worker.WorkerHomeScreen
import kotlinx.serialization.Serializable

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Login
    ) {
        composable<Login> {
            LoginScreen(
                onRegisterClick = { navController.navigate(Register) },
                onWorkerLoginSuccess = {
                    navController.navigate(WorkerHome) {
                        popUpTo(Login) { inclusive = true }
                    }
                },
                onClientLoginSuccess = {
                    navController.navigate(ClientHome) {
                        popUpTo(Login) { inclusive = true }
                    }
                }
            )
        }

        composable<Register> {
            RegisterScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(Register) { inclusive = true }
                    }
                },
                onWorkerRegisterClick = { navController.navigate(RegisterWorker) }
            )
        }

        composable<RegisterWorker> {
            RegisterWorkerScreen(
                onBackClick = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate(Login) {
                        popUpTo(RegisterWorker) { inclusive = true }
                    }
                }
            )
        }

        composable<WorkerHome> {
            WorkerHomeScreen()
        }

        composable<ClientHome> {
            ClientScaffold(
                onCategoryClick = { categoryId ->
                    navController.navigate(CategoryDetail(categoryId))
                }
            )
        }

        composable<CategoryDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<CategoryDetail>()
            CategoryDetailScreen(
                categoryId = args.categoryId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

@Serializable
object Login

@Serializable
object Register

@Serializable
object RegisterWorker

@Serializable
object WorkerHome

@Serializable
object ClientHome

@Serializable
data class CategoryDetail(val categoryId: String)