package dev.baharudin.weatherinfo.domain.usecases.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import dev.baharudin.weatherinfo.domain.entities.Coordinate
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.entities.Resource
import dev.baharudin.weatherinfo.domain.repositories.LocationRepository
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.internal.matchers.apachecommons.ReflectionEquals

class GetSavedLocationTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockLocationRepository: LocationRepository

    private lateinit var getSavedLocation: GetSavedLocation

    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        getSavedLocation = GetSavedLocation(mockLocationRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when invoke success should return the list`() = runTest {
        val location = Location("city", "state", "country", Coordinate(0f, 0f))
        Mockito.`when`(mockLocationRepository.getSavedLocation())
            .thenReturn(flowOf(listOf(location)))

        getSavedLocation().test {
            TestCase.assertTrue(
                ReflectionEquals(awaitItem()).matches(
                    Resource.Success(listOf(location))
                )
            )
            awaitComplete()
        }
    }
}