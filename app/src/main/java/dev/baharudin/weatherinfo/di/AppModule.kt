package dev.baharudin.weatherinfo.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.baharudin.weatherinfo.BuildConfig
import dev.baharudin.weatherinfo.data.data_sources.api.LocationApi
import dev.baharudin.weatherinfo.data.data_sources.api.WeatherApi
import dev.baharudin.weatherinfo.data.data_sources.db.LocationDao
import dev.baharudin.weatherinfo.data.data_sources.db.LocationDatabase
import dev.baharudin.weatherinfo.data.repositories.LocationRepositoryImpl
import dev.baharudin.weatherinfo.data.repositories.WeatherRepositoryImpl
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocationDatabase(@ApplicationContext context: Context): LocationDatabase =
        Room.databaseBuilder(
            context, LocationDatabase::class.java, LocationDatabase.NAME
        )
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideLocationDao(locationDatabase: LocationDatabase): LocationDao =
        locationDatabase.locationDao()

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient = OkHttpClient.Builder().readTimeout(15, TimeUnit.SECONDS)
        .connectTimeout(15, TimeUnit.SECONDS).addInterceptor { chain ->
            val url =
                chain.request().url.newBuilder().addQueryParameter("appid", BuildConfig.API_KEY)
                    .build()
            val request = chain.request().newBuilder().url(url).build()
            chain.proceed(request)
        }.build()

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory
    ): Retrofit = Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).client(okHttpClient)
        .addConverterFactory(gsonConverterFactory).build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideLocationApi(retrofit: Retrofit): LocationApi =
        retrofit.create(LocationApi::class.java)

    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApi: WeatherApi
    ): WeatherRepository =
        WeatherRepositoryImpl(weatherApi)

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationDao: LocationDao,
        locationApi: LocationApi
    ): LocationRepository =
        LocationRepositoryImpl(locationDao, locationApi)
}