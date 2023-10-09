package dev.baharudin.weatherinfo.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.weatherinfo.presentation.common.DataState
import dev.baharudin.weatherinfo.core.Resource
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.usecases.condition.GetCurrentCondition
import dev.baharudin.weatherinfo.domain.usecases.location.GetSavedLocation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val _getSavedLocation: GetSavedLocation,
    private val _getCurrentCondition: GetCurrentCondition,
) : ViewModel() {

    companion object {
        private const val TAG = "HomeViewModel"
    }

    private val _savedLocationList = MutableLiveData<List<Location>>()
    private val _savedConditionList = MutableLiveData(DataState<ArrayList<Condition>>())

    val savedConditionList: LiveData<DataState<ArrayList<Condition>>> = _savedConditionList

    init {
        viewModelScope.launch {
            _getSavedLocation().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        Log.d(TAG, "init: ${resource.data}")
                        refreshConditionList(resource.data)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun refreshConditionList(locationList: List<Location>) {
        _savedLocationList.value = locationList
        if (_savedConditionList.value == null || _savedConditionList.value!!.data.isNullOrEmpty()) {
            val conditions = locationList.map { Condition(location = it) }
            _savedConditionList.value = DataState(data = ArrayList(conditions))
            updateCondition(
                *locationList.toTypedArray()
            )
        }

        val savedConditionSize = _savedConditionList.value?.data?.size ?: 0
        val savedLocationSize = _savedLocationList.value?.size ?: 0

        val newCondition =
            if (savedConditionSize == 0) DataState(data = arrayListOf()) else _savedConditionList.value!!
        var i = 0

        // User add new location
        if (savedLocationSize > savedConditionSize) {
            while (i < savedLocationSize) {
                val savedLocation = locationList[i]
                val fetchedCondition = newCondition.data!![i]
                if (savedLocation.getName() != fetchedCondition.location.getName()) {
                    newCondition.data.add(i, Condition(location = savedLocation))
                    updateCondition(savedLocation)
                }
                i++
            }
        }
        // User remove some location
        else if (savedLocationSize < savedConditionSize) {
            while (i < savedLocationSize) {
                val savedLocation = locationList[i]
                val fetchedCondition = newCondition.data!![i]
                if (savedLocation.getName() != fetchedCondition.location.getName()) {
                    newCondition.data.removeAt(i)
                } else {
                    i++
                }
            }
        }

        _savedConditionList.value = newCondition
    }

    private fun updateCondition(vararg locations: Location) {
        locations.forEach { location ->
            _getCurrentCondition(location).onEach { resource ->
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