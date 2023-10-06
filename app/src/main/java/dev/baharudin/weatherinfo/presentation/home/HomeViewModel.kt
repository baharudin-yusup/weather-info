package dev.baharudin.weatherinfo.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.baharudin.weatherinfo.common.Resource
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.usecases.condition.GetCurrentCondition
import dev.baharudin.weatherinfo.domain.usecases.location.GetSavedLocation
import dev.baharudin.weatherinfo.domain.usecases.location.RemoveSavedLocation
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getSavedLocation: GetSavedLocation,
    private val removeSavedLocation: RemoveSavedLocation,
    private val getCurrentCondition: GetCurrentCondition,
) : ViewModel() {
    private val _savedLocationList = MutableLiveData<List<Location>>()
    private val _savedConditionList = MutableLiveData<ArrayList<Condition>>()

    val savedConditionList: LiveData<ArrayList<Condition>> = _savedConditionList

    init {
        viewModelScope.launch {
            getSavedLocation().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        if (_savedConditionList.value == null)
                            refreshConditionList(resource.data)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun refreshConditionList(locationList: List<Location>) {
        if (!_savedConditionList.isInitialized || _savedConditionList.value!!.isEmpty())
            updateCondition(*locationList.toTypedArray())

        val savedConditionSize = _savedConditionList.value?.size ?: 0
        val savedLocationSize = _savedLocationList.value?.size ?: 0

        val newCondition = _savedConditionList.value!!
        var i = 0

        // User add new location
        if (savedLocationSize > savedConditionSize) {
            while (i < savedLocationSize) {
                val savedLocation = locationList[i]
                val fetchedCondition = newCondition[i]
                if (savedLocation.getName() != fetchedCondition.location.getName()) {
                    newCondition.add(i, Condition(location = savedLocation))
                }
                i++
            }
        }
        // User remove some location
        else if (savedLocationSize < savedConditionSize) {
            while (i < savedLocationSize) {
                val savedLocation = locationList[i]
                val fetchedCondition = newCondition[i]
                if (savedLocation.getName() != fetchedCondition.location.getName()) {
                    newCondition.removeAt(i)
                } else {
                    i++
                }
            }
        }

        _savedConditionList.value = newCondition
    }

    fun updateCondition(vararg locations: Location) {
        return
        locations.forEach { location ->
            getCurrentCondition(location).onEach { resource ->
                when (resource) {
                    is Resource.Error -> {

                    }

                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        val index = _savedConditionList.value?.indexOfFirst {
                            it.location.getName() == location.getName()
                        } ?: -1

                        if (index >= 0) {
                            _savedConditionList.value?.set(index, resource.data)
                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun removeLocation(location: Location) {
        removeSavedLocation(location).onEach { resource ->
//            when (resource) {
//                is Resource.Error -> TODO()
//                is Resource.Loading -> TODO()
//                is Resource.Success -> TODO()
//            }
        }.launchIn(viewModelScope)
    }
}