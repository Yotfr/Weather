package com.yotfr.weather.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.yotfr.weather.data.weather.location.DefLocationTracker
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestDefLocationTracker {

    @Test
    fun gewgwe() = runTest {
        val application = createApplication()
        val defLocationTracker = createDefLocationTracker()
        val locationManager = createLocationManager()
        val context = createApplicationContext()

        coEvery {
            application.applicationContext
        } returns context

        coEvery {
            ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } returns PackageManager.PERMISSION_DENIED

        coEvery {
            application.getSystemService(
                Context.LOCATION_SERVICE
            )
        } returns mockk()

        coEvery {
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } returns true

        coEvery {
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } returns true

        coEvery {
            ContextCompat.checkSelfPermission(
                application,
                any()
            )
        } returns PackageManager.PERMISSION_DENIED

        val result = defLocationTracker.getCurrentLocation()

        assertNull(result)
    }

    private fun createDefLocationTracker(): DefLocationTracker {
        return DefLocationTracker(
            locationClient = createLocationClient(),
            application = createApplication()
        )
    }

    private fun createApplication(): Application {
        return mockk(relaxed = true)
    }

    private fun createLocationClient(): FusedLocationProviderClient {
        return mockk()
    }

    private fun createLocationManager(): LocationManager {
        return mockk(relaxed = true)
    }

    private fun createApplicationContext(): Context {
        return mockk()
    }
}
