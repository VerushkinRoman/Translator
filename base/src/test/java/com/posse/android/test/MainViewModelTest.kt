package com.posse.android.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.posse.android.base.MainViewModel
import com.posse.android.data.Interactor
import com.posse.android.data.MainInteractor
import com.posse.android.data.repository.Repository
import com.posse.android.models.AppState
import com.posse.android.models.DataModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.lang.Exception
import java.lang.RuntimeException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class MainViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var interactor: Interactor<AppState>

    @Mock
    private lateinit var localRepository: Repository<List<DataModel>>

    @Mock
    private lateinit var remoteRepository: Repository<List<DataModel>>

    @Mock
    private lateinit var observer: Observer<AppState>

    @Captor
    private lateinit var argumentCaptor: ArgumentCaptor<AppState>

    private lateinit var mainViewModel: MainViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = MainInteractor(remoteRepository, localRepository)
        Dispatchers.setMain(TestCoroutineDispatcher())
        mainViewModel = MainViewModel(interactor)
    }

    @Test
    fun getAppStateError_whenReturnThrowable() {
        val isOnline = true
        val testWord = "Test"
        runBlocking {
//            `when`(interactor.getData(testWord, isOnline)).thenThrow(Exception())
            `when`(interactor.getData(testWord, isOnline)).thenReturn(
                AppState.Error(
                    RuntimeException()
                )
            )
        }
//            doThrow(RuntimeException()).`when`(interactor).getData(testWord,isOnline)
            mainViewModel.getWordDescriptions(testWord, isOnline)
//        mainViewModel.getStateLiveData().observeForever(observer)

//        verify(observer, times(1))
//            .onChanged(argumentCaptor.capture())

//        observer.onChanged( argumentCaptor.capture())

//        val values = argumentCaptor.allValues

            MatcherAssert.assertThat(
                mainViewModel.getStateLiveData().getOrAwaitValue(),
                instanceOf(AppState.Error::class.java)
            )

//        argumentCaptor.value.also { MatcherAssert.assertThat(it, instanceOf(AppState.Error::class.java)) }

//        verify(observer, times(1))
//            .onChanged(argumentCaptor.capture())
    }

    fun <T> LiveData<T>.getOrAwaitValue(
        time: Long = 2,
        timeUnit: TimeUnit = TimeUnit.SECONDS
    ): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                if (o != null) {
                    data = o
                    latch.countDown()
                    this@getOrAwaitValue.removeObserver(this)
                }
            }
        }

        this.observeForever(observer)

        // Don't wait indefinitely if the LiveData is not set.
        if (!latch.await(time, timeUnit)) {
            throw TimeoutException("LiveData value was never set.")
        }

        @Suppress("UNCHECKED_CAST")
        return data as T
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}