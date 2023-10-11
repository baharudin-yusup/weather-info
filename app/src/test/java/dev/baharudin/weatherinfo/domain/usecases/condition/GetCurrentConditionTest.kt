package dev.baharudin.weatherinfo.domain.usecases.condition

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Coordinate
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.domain.entities.Resource
import dev.baharudin.weatherinfo.domain.repositories.WeatherRepository
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.internal.matchers.apachecommons.ReflectionEquals
import retrofit2.HttpException

class GetCurrentConditionTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockWeatherRepository: WeatherRepository

    @Mock
    private lateinit var mockHttpException: HttpException
    private lateinit var getCurrentCondition: GetCurrentCondition


    private val testDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        getCurrentCondition = GetCurrentCondition(mockWeatherRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when invoke success should return the condition object`() = runTest {
        val location = Location("city", "state", "country", Coordinate(0f, 0f))
        val condition = Condition(location = location)

        `when`(mockWeatherRepository.getCurrentCondition(location)).thenReturn(condition)

        getCurrentCondition(location).test {
            assertTrue(ReflectionEquals(awaitItem()).matches(Resource.Loading<Condition>()))
            assertTrue(ReflectionEquals(awaitItem()).matches(Resource.Success(condition)))
            awaitComplete()
        }
    }

    @Test
    fun `when requested location not found should return 404 error message`() = runTest {
        val location = Location("city", "state", "country", Coordinate(0f, 0f))
        val errorMessage = "404 - Not Found"

        `when`(mockHttpException.localizedMessage).thenReturn(errorMessage)
        `when`(mockWeatherRepository.getCurrentCondition(location)).thenThrow(mockHttpException)

        getCurrentCondition(location).test {
            assertTrue(ReflectionEquals(awaitItem()).matches(Resource.Loading<Condition>()))
            assertTrue(ReflectionEquals(awaitItem()).matches(Resource.Error<Condition>(errorMessage)))
            awaitComplete()
        }
    }
}