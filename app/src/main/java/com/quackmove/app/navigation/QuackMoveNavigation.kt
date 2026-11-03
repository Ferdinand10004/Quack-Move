package com.quackmove.app.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.quackmove.app.feature.chargers.ChargersScreen
import com.quackmove.app.feature.registration.RegistrationScreen
import com.quackmove.app.feature.routes.RoutesScreen

object QuackMoveRoutes {
    const val MAP = "map"
    const val REGISTRATION = "registration"
    const val CHARGERS = "chargers"
    const val ROUTES = "routes"
    const val ROUTE_DESTINATION = "routes/{destination}"
}

@Composable
fun QuackMoveNavigation(
    navController: NavHostController,
    mapScreen: @Composable (
        onOpenRegistration: () -> Unit,
        onOpenChargers: () -> Unit,
        onOpenRoute: (String) -> Unit
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = QuackMoveRoutes.MAP
    ) {

        composable(QuackMoveRoutes.MAP) {

            mapScreen(
                {
                    navController.navigate(
                        QuackMoveRoutes.REGISTRATION
                    )
                },
                {
                    navController.navigate(
                        QuackMoveRoutes.CHARGERS
                    )
                },
                { destination ->
                    navController.navigate(
                        "${QuackMoveRoutes.ROUTES}/${Uri.encode(destination)}"
                    )
                }
            )
        }

        composable(QuackMoveRoutes.REGISTRATION) {

            RegistrationScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(QuackMoveRoutes.CHARGERS) {

            ChargersScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = QuackMoveRoutes.ROUTE_DESTINATION,
            arguments = listOf(
                navArgument("destination") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val destination = backStackEntry
                .arguments
                ?.getString("destination")
                ?: "Destino seleccionado"

            RoutesScreen(
                destination = destination,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}