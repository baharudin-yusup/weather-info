package dev.baharudin.weatherinfo.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.entities.Resource
import dev.baharudin.weatherinfo.domain.usecases.condition.GetCurrentCondition
import dev.baharudin.weatherinfo.domain.usecases.location.GetSavedLocation
import dev.baharudin.weatherinfo.presentation.common.DataState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSavedLocation: GetSavedLocation,
    private val getCurrentCondition: GetCurrentCondition,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _savedLocationList = MutableLiveData<List<Location>>()
    private val _savedConditionList = MutableLiveData(DataState<ArrayList<Condition>>())

    val savedConditionList: LiveData<DataState<ArrayList<Condition>>> = _savedConditionList

    init {
        viewModelScope.launch {
            getSavedLocation().collect { updateSavedLocation(it) }
        }
    }

    private fun updateSavedLocation(resource: Resource<List<Location>>) {
        when (resource) {
            is Resource.Success -> {
                mapLocationToCondition(resource.data)
            }

            is Resource.Loading -> {
                _savedConditionList.value =
                    _savedConditionList.value?.copy(isLoading = true)
            }

            is Resource.Error -> {
                _savedConditionList.value =
                    _savedConditionList.value?.copy(
                        isLoading = false,
                        message = resource.message
                    )
            }
        }
    }

    private fun mapLocationToCondition(locationList: List<Location>) {
        _savedLocationList.value = locationList

        if (_savedLocationList.value.isNullOrEmpty()) {
            _savedConditionList.value = DataState(data = arrayListOf())
            return
        }

        if (_savedConditionList.value == null || _savedConditionList.value?.data.isNullOrEmpty()) {
            val conditions = locationList.map { Condition(location = it) }
            _savedConditionList.value = DataState(data = ArrayList(conditions))
            updateCurrentCondition(
                *locationList.toTypedArray()
            )
        }

        val savedConditionSize = _savedConditionList.value?.data?.size ?: 0
        val savedLocationSize = _savedLocationList.value?.size ?: 0


        // User add new location
        if (savedLocationSize > savedConditionSize) {
            val newCondition =
                if (savedConditionSize == 0) DataState(data = arrayListOf()) else _savedConditionList.value!!
            var i = 0
            while (i < savedLocationSize) {
                val savedLocation = locationList[i]
                val fetchedCondition = newCondition.data!![i]
                if (savedLocation.getName() != fetchedCondition.location.getName()) {
                    newCondition.data.add(i, Condition(location = savedLocation))
                    updateCurrentCondition(savedLocation)
                }
                i++
            }
            _savedConditionList.value = newCondition
        }
        // User remove some location
        else if (savedLocationSize < savedConditionSize) {
            val savedLocationName = locationList.map { it.getName() }
            val savedConditions = _savedConditionList.value?.data
            savedConditions?.removeAll { !savedLocationName.contains(it.location.getName()) }
            _savedConditionList.value = DataState(data = savedConditions)
        }

    }

    private fun updateCurrentCondition(vararg locations: Location) {
        locations.forEach { location ->
            getCurrentCondition(location).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        val index = _savedConditionList.value?.data?.indexOfFirst {
                            it.location.getName() == location.getName()
                        } ?: -1

                        Log.d(TAG, "updateCondition: $index | ${resource.data}")
                        if (index >= 0) {
                            val newList = _savedConditionList.value?.data ?: arrayListOf()
                            newList[index] = resource.data.copy(location = location)
                            _savedConditionList.postValue(_savedConditionList.value?.copy(data = newList))
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}