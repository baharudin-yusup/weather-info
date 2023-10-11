package dev.baharudin.weatherinfo.data.sources.api

import dev.baharudin.weatherinfo.data.sources.api.models.LocationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("/geo/1.0/direct")
    suspend fun searchLocation(
        @Query("q") location: String,
        @Query("limit") limit: Int = 5,
    ): List<LocationResponse>
}