package dev.baharudin.weatherinfo.common

data class DataState<T> (
    val data: T? = null,
    val isLoading: Boolean = false,
    val message: String = ""
)