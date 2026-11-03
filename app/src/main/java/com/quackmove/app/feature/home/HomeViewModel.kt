package com.quackmove.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quackmove.app.data.model.RecentDestination
import com.quackmove.app.data.repository.DemoDestinationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la pantalla principal.
data class HomeUiState(
    val searchText: String = "",
    val darkMap: Boolean = true,
    val recentDestinations: List<RecentDestination> =
        emptyList(),
    val searchResults: List<Pair<String, String>> =
        emptyList()
)

class HomeViewModel(
    private val destinationsRepository: DemoDestinationsRepository
) : ViewModel() {

    // Estado privado.
    private val _uiState = MutableStateFlow(
        HomeUiState(
            recentDestinations =
                destinationsRepository.demoRecentDestinations
        )
    )

    // Estado público de solo lectura.
    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()

    // Actualizar texto y resultados de búsqueda.
    fun updateSearchText(value: String) {

        val search = value.trim()

        val results = if (search.isBlank()) {
            emptyList()
        } else {
            destinationsRepository.searchablePlaces.filter { place ->

                place.first.contains(
                    search,
                    ignoreCase = true
                ) ||
                        place.second.contains(
                            search,
                            ignoreCase = true
                        )
            }
        }

        _uiState.update { current ->
            current.copy(
                searchText = value,
                searchResults = results
            )
        }
    }

    // Cambiar el estilo del mapa.
    fun toggleDarkMap(): Boolean {

        val newDarkMap = !_uiState.value.darkMap

        _uiState.update { current ->
            current.copy(
                darkMap = newDarkMap
            )
        }

        return newDarkMap
    }
}

class HomeViewModelFactory(
    private val repository: DemoDestinationsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                HomeViewModel::class.java
            )
        ) {

            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                destinationsRepository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
