package com.posse.android.test

import com.posse.android.data.Interactor
import com.posse.android.data.MainInteractor
import com.posse.android.data.repository.Repository
import com.posse.android.models.AppState
import com.posse.android.models.DataModel
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class InteractorTest {

    @Mock
    private lateinit var localRepository: Repository<List<DataModel>>

    @Mock
    private lateinit var remoteRepository: Repository<List<DataModel>>

    private lateinit var interactor: Interactor<AppState>

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = MainInteractor(remoteRepository, localRepository)
    }

    @Test
    fun returnAppState_whenOnline() {
        val isOnline = true
        val testWord = "Test"
        val returnData = emptyList<DataModel>()
        runBlocking {
            `when`(remoteRepository.getData(testWord)).thenReturn(returnData)
            MatcherAssert.assertThat(interactor.getData(testWord, isOnline), instanceOf(AppState::class.java))
        }
    }

    @Test
    fun returnAppState_whenOffline() {
        val isOnline = false
        val testWord = "Test"
        val returnData = emptyList<DataModel>()
        runBlocking {
            `when`(localRepository.getData(testWord)).thenReturn(returnData)
            MatcherAssert.assertThat(interactor.getData(testWord, isOnline), instanceOf(AppState::class.java))
        }
    }
}