package dev.baharudin.weatherinfo.presentation.common

data class DataState<T> (
    val data: T? = null,
    val isLoading: Boolean = false,
    val message: String = ""
)