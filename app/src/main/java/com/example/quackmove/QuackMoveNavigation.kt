
package com.example.quackmove

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

object QuackMoveRoutes {
    const val MAPA = "mapa"
    const val REGISTRO = "registro"
    const val CARGADORES = "cargadores"
    const val RUTAS = "rutas"
    const val RUTAS_DESTINO = "rutas/{destino}"
}

@Composable
fun QuackMoveNavigation(
    navController: NavHostController,
    pantallaMapa: @Composable (
        onAbrirRegistro: () -> Unit,
        onAbrirCargadores: () -> Unit,
        onAbrirRuta: (String) -> Unit
    ) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = QuackMoveRoutes.MAPA
    ) {

        composable(QuackMoveRoutes.MAPA) {

            pantallaMapa(
                {
                    navController.navigate(
                        QuackMoveRoutes.REGISTRO
                    )
                },
                {
                    navController.navigate(
                        QuackMoveRoutes.CARGADORES
                    )
                },
                { destino ->
                    navController.navigate(
                        "${QuackMoveRoutes.RUTAS}/${Uri.encode(destino)}"
                    )
                }
            )
        }

        composable(QuackMoveRoutes.REGISTRO) {

            RegistroScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(QuackMoveRoutes.CARGADORES) {

            CargadoresScreen(
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = QuackMoveRoutes.RUTAS_DESTINO,
            arguments = listOf(
                navArgument("destino") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val destino = backStackEntry
                .arguments
                ?.getString("destino")
                ?: "Destino seleccionado"

            RutasScreen(
                destino = destino,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }
    }
}