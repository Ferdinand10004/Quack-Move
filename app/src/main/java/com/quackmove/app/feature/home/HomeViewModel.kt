package com.quackmove.app.feature.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.quackmove.app.data.repository.DemoDestinationsRepository

class HomeViewModel : ViewModel() {
    var searchText by mutableStateOf("")
        private set
    var darkMap by mutableStateOf(true)
        private set

    val recentDestinations get() = DemoDestinationsRepository.demoRecentDestinations
    val searchResults: List<Pair<String, String>>
        get() = DemoDestinationsRepository.searchablePlaces.filter { place ->
            place.first.contains(searchText.trim(), ignoreCase = true) ||
                place.second.contains(searchText.trim(), ignoreCase = true)
        }

    fun updateSearchText(value: String) { searchText = value }
    fun toggleDarkMap() { darkMap = !darkMap }
}
