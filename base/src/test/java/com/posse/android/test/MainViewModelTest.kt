package com.posse.android.test

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.posse.android.base.MainViewModel
import com.posse.android.data.Interactor
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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class MainViewModelTest {

    @Rule
    @JvmField
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var interactor: Interactor<AppState>

    private lateinit var mainViewModel: MainViewModel

    private lateinit var closeable: AutoCloseable

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        closeable = MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(TestCoroutineDispatcher())
        mainViewModel = MainViewModel(interactor)
    }

    @Test
    fun getAppStateError_whenReturnThrowable() {
        val isOnline = true
        val testWord = "Test"
        runBlocking {
            `when`(interactor.getData(testWord, isOnline)).thenThrow(RuntimeException())
        }
        mainViewModel.getWordDescriptions(testWord, isOnline)

        MatcherAssert.assertThat(
            mainViewModel.getStateLiveData().value,
            instanceOf(AppState.Error::class.java)
        )
    }

    @Test
    fun getAppStateSuccess_whenReturnData() {
        val isOnline = true
        val testWord = "Test"
        val data = AppState.Success(emptyList())
        runBlocking {
            `when`(interactor.getData(testWord, isOnline)).thenReturn(data)
        }
        mainViewModel.getWordDescriptions(testWord, isOnline)

        MatcherAssert.assertThat(
            mainViewModel.getStateLiveData().value,
            instanceOf(AppState.Success::class.java)
        )
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        closeable.close()
    }
}