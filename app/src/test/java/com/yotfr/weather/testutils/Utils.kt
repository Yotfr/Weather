package com.yotfr.weather.testutils

import org.junit.Assert

inline fun <reified T: Throwable> catch(block: () -> Unit):T {
    try {
        block()
    }catch (e:Throwable) {
        if (e is T) {
            return e
        } else {
            Assert.fail(
                "invalid exception type" +
                        "Expected: ${T::class.java.simpleName}" +
                        "Actual: ${e.javaClass.simpleName}"
            )
        }
    }
    throw AssertionError("Expected an exception, but received nothing")
}