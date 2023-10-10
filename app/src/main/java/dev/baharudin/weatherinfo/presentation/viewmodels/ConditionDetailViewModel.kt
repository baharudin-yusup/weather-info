package dev.baharudin.weatherinfo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dev.baharudin.weatherinfo.presentation.common.DataState
import dev.baharudin.weatherinfo.domain.entities.Resource
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.usecases.condition.GetCurrentCondition
import dev.baharudin.weatherinfo.domain.usecases.condition.GetDailyForecastCondition
import dev.baharudin.weatherinfo.domain.usecases.condition.GetHourlyForecastCondition
import dev.baharudin.weatherinfo.domain.usecases.location.GetIsLocationSaved
import dev.baharudin.weatherinfo.domain.usecases.location.RemoveSavedLocation
import dev.baharudin.weatherinfo.domain.usecases.location.SaveLocation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ConditionDetailViewModel @AssistedInject constructor(
    private val getCurrentCondition: GetCurrentCondition,
    private val getHourlyForecastCondition: GetHourlyForecastCondition,
    private val getDailyForecastCondition: GetDailyForecastCondition,
    private val saveLocation: SaveLocation,
    private val removeSavedLocation: RemoveSavedLocation,
    private val getIsLocationSaved: GetIsLocationSaved,
    @Assisted private val initialCondition: Condition,
) : ViewModel() {

    @Suppress("UNCHECKED_CAST")
    companion object {
        private const val TAG = "ConditionDetailViewModel"
        fun providesFactory(
            factory: Factory,
            condition: Condition,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(condition) as T
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(condition: Condition): ConditionDetailViewModel
    }

    private val _currentConditionState =
        MutableLiveData(DataState(data = initialCondition))
    private val _hourlyForecastConditionState = MutableLiveData(DataState<ArrayList<Condition>>())
    private val _dailyForecastConditionState = MutableLiveData(DataState<ArrayList<Condition>>())
    private val _isLocationSaved = MutableLiveData(DataState<Boolean>())

    val currentConditionState: LiveData<DataState<Condition>> = _currentConditionState
    val hourlyForecastConditionState: LiveData<DataState<ArrayList<Condition>>> =
        _hourlyForecastConditionState
    val dailyForecastConditionState: LiveData<DataState<ArrayList<Condition>>> =
        _dailyForecastConditionState
    val isLocationSaved: LiveData<DataState<Boolean>> = _isLocationSaved

    init {
        viewModelScope.launch {
            getIsLocationSaved(initialCondition.location).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _isLocationSaved.value = DataState(data = resource.data)
                    }

                    else -> {}
                }
            }
        }
        refreshData()
    }

    private fun refreshData() {
        fetchCurrentCondition()
        fetchHourlyForecastCondition()
        fetchDailyForecastCondition()
    }

    private fun fetchCurrentCondition() {
        getCurrentCondition(initialCondition.location).onEach { resource ->
            Log.d(TAG, "fetchCurrentCondition: $resource")
            when (resource) {
                is Resource.Success -> {
                    _currentConditionState.value =
                        DataState(data = resource.data.copy(location = initialCondition.location))
                }

                is Resource.Error -> {
                    _currentConditionState.value = DataState(message = resource.message)
                }

                is Resource.Loading -> {
                    _currentConditionState.value = DataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchHourlyForecastCondition() {
        getHourlyForecastCondition(initialCondition.location).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _hourlyForecastConditionState.value =
                        DataState(data = ArrayList(resource.data.take(8)))
                }

                is Resource.Error -> {
                    _hourlyForecastConditionState.value = DataState(message = resource.message)
                }

                is Resource.Loading -> {
                    _hourlyForecastConditionState.value = DataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun fetchDailyForecastCondition() {
        getDailyForecastCondition(initialCondition.location).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    _dailyForecastConditionState.value = DataState(data = ArrayList(resource.data))
                }

                is Resource.Error -> {
                    _dailyForecastConditionState.value = DataState(message = resource.message)
                }

                is Resource.Loading -> {
                    _dailyForecastConditionState.value = DataState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onSaveButtonClick() {
        val isSaved = _isLocationSaved.value?.data

        if (isSaved == null) {
            _isLocationSaved.value = DataState(message = "Please retry again...")
            return
        }

        when (isSaved) {
            true -> {
                removeSavedLocation(initialCondition.location).onEach { resource ->
                    when (resource) {
                        is Resource.Error -> _isLocationSaved.value =
                            DataState(message = resource.message)

                        is Resource.Loading -> _isLocationSaved.value = DataState(isLoading = true)
                        is Resource.Success -> _isLocationSaved.value = DataState(
                            data = false,
                            message = "Location ${initialCondition.location.city} has been removed to the favorites list"
                        )
                    }
                }.launchIn(viewModelScope)
            }

            false -> {
                saveLocation(initialCondition.location).onEach { resource ->
                    when (resource) {
                        is Resource.Error -> _isLocationSaved.value =
                            DataState(message = resource.message)

                        is Resource.Loading -> _isLocationSaved.value = DataState(isLoading = true)
                        is Resource.Success -> _isLocationSaved.value = DataState(
                            data = true,
                            message = "Location ${initialCondition.location.city} has been added to the favorites list"
                        )
                    }
                }.launchIn(viewModelScope)
            }
        }
    }
}