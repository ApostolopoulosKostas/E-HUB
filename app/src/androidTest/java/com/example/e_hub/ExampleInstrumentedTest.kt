package com.example.e_hub

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * INSTRUMENTED TEST THAT RUNS ON AN ANDROID DEVICE.
 *
 * SEE [TESTING DOCUMENTATION](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // GET THE CONTEXT OF THE APP UNDER TEST.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.e_hub", appContext.packageName)
    }
}