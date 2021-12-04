package com.posse.android.test

import com.posse.android.main.MainFragment
import org.junit.Test
import org.junit.Assert.*

class TextValidatorTest {

    @Test
    fun validator_CorrectText_ReturnsTrue(){
       assertTrue(MainFragment.checkValidText("test"))
    }

    @Test
    fun validator_SpaceInText_ReturnsFalse(){
        assertFalse(MainFragment.checkValidText("test this function"))
    }

    @Test
    fun validator_EmptyText_ReturnsFalse(){
        assertFalse(MainFragment.checkValidText(""))
    }
}