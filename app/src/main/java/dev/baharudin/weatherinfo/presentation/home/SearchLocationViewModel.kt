package dev.baharudin.weatherinfo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.weatherinfo.common.DataState
import dev.baharudin.weatherinfo.common.Resource
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.usecases.location.SearchLocation
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchLocationViewModel @Inject constructor(
    private val searchLocation: SearchLocation,
) : ViewModel() {

    companion object {
        private const val TAG = "SearchLocationViewModel"
    }

    private val _searchQuery = MutableStateFlow("")

    private val _searchLocationState = MutableLiveData(DataState<ArrayList<Location>>())
    val state: LiveData<DataState<ArrayList<Location>>> = _searchLocationState

    init {
        _searchQuery.debounce(300L).map { it.trim() }.distinctUntilChanged().onEach {
            if (it.isBlank()) {
                _searchLocationState.value = DataState(data = arrayListOf())
            } else {
                searchLocation(it).onEach { resource ->
                    when (resource) {
                        is Resource.Error -> _searchLocationState.value =
                            DataState(message = resource.message)

                        is Resource.Loading -> _searchLocationState.value =
                            _searchLocationState.value?.copy(isLoading = true, message = "")

                        is Resource.Success -> _searchLocationState.value = DataState(
                            data = filterDuplicateCityInCountry(resource.data),
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }.launchIn(viewModelScope)
    }

    fun search(name: String) {
        _searchQuery.value = name
    }

    private fun filterDuplicateCityInCountry(locations: List<Location>): ArrayList<Location> {
        val addedLocation = arrayListOf<String>()
        val output = arrayListOf<Location>()

        locations.forEach { location ->
            val query = "${location.city},${location.country}"

            if (!addedLocation.contains(query)) {
                output.add(location)
                addedLocation.add(query)
            }
        }

        return output
    }
}