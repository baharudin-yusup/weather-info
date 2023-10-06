package dev.baharudin.weatherinfo.common

sealed class Resource<T> {
    class Success<T>(val data: T, val message: String = "") : Resource<T>()
    class Error<T>(val message: String) : Resource<T>()
    class Loading<T> : Resource<T>()
}