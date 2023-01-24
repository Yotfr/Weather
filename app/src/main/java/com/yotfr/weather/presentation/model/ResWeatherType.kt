package com.yotfr.weather.presentation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yotfr.weather.R
import com.yotfr.weather.domain.model.WeatherType

sealed class ResWeatherType(
    val id: Int,
    @StringRes val weatherDesc: Int,
    @DrawableRes val iconRes: Int
) {
    object ClearSkySun : ResWeatherType(
        weatherDesc = "Clear sky",
        iconRes = R.drawable.ic_clear_sky_sun
    )
    object ClearSkyMoon : ResWeatherType(
        weatherDesc = "Clear sky",
        iconRes = R.drawable.ic_clear_sky_moon
    )
    object MainlyClearSun : ResWeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.ic_mainly_clear_sun
    )
    object MainlyClearMoon : ResWeatherType(
        weatherDesc = "Mainly clear",
        iconRes = R.drawable.ic_mainly_clear_moon
    )
    object PartlyCloudySun : ResWeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.ic_partly_cloudy_sun
    )
    object PartlyCloudyMoon : ResWeatherType(
        weatherDesc = "Partly cloudy",
        iconRes = R.drawable.ic_partly_cloudy_moon
    )
    object Overcast : ResWeatherType(
        weatherDesc = "Overcast",
        iconRes = R.drawable.ic_cloudy
    )
    object Foggy : ResWeatherType(
        weatherDesc = "Foggy",
        iconRes = R.drawable.ic_foggy
    )
    object DepositingRimeFog : ResWeatherType(
        weatherDesc = "Depositing rime fog",
        iconRes = R.drawable.ic_foggy
    )
    object LightDrizzle : ResWeatherType(
        weatherDesc = "Light drizzle",
        iconRes = R.drawable.ic_drizzle
    )
    object ModerateDrizzle : ResWeatherType(
        weatherDesc = "Moderate drizzle",
        iconRes = R.drawable.ic_drizzle
    )
    object DenseDrizzle : ResWeatherType(
        weatherDesc = "Dense drizzle",
        iconRes = R.drawable.ic_drizzle
    )
    object LightFreezingDrizzle : ResWeatherType(
        weatherDesc = "Slight freezing drizzle",
        iconRes = R.drawable.ic_sleet
    )
    object DenseFreezingDrizzle : ResWeatherType(
        weatherDesc = "Dense freezing drizzle",
        iconRes = R.drawable.ic_sleet
    )
    object SlightRain : ResWeatherType(
        weatherDesc = "Slight rain",
        iconRes = R.drawable.ic_rainy
    )
    object ModerateRain : ResWeatherType(
        weatherDesc = "Rainy",
        iconRes = R.drawable.ic_rainy
    )
    object HeavyRain : ResWeatherType(
        weatherDesc = "Heavy rain",
        iconRes = R.drawable.ic_heavyrainy
    )
    object HeavyFreezingRain : ResWeatherType(
        weatherDesc = "Heavy freezing rain",
        iconRes = R.drawable.ic_sleet
    )
    object SlightSnowFall : ResWeatherType(
        weatherDesc = "Slight snow fall",
        iconRes = R.drawable.ic_snowy
    )
    object ModerateSnowFall : ResWeatherType(
        weatherDesc = "Moderate snow fall",
        iconRes = R.drawable.ic_snowy
    )
    object HeavySnowFall : ResWeatherType(
        weatherDesc = "Heavy snow fall",
        iconRes = R.drawable.ic_snowy
    )
    object SnowGrains : ResWeatherType(
        weatherDesc = "Snow grains",
        iconRes = R.drawable.ic_hail
    )
    object SlightRainShowers : ResWeatherType(
        weatherDesc = "Slight rain showers",
        iconRes = R.drawable.ic_rainy
    )
    object ModerateRainShowers : ResWeatherType(
        weatherDesc = "Moderate rain showers",
        iconRes = R.drawable.ic_rainy
    )
    object ViolentRainShowers : ResWeatherType(
        weatherDesc = "Violent rain showers",
        iconRes = R.drawable.ic_heavyrainy
    )
    object SlightSnowShowers : ResWeatherType(
        weatherDesc = "Light snow showers",
        iconRes = R.drawable.ic_heavysnow
    )
    object HeavySnowShowers : ResWeatherType(
        weatherDesc = "Heavy snow showers",
        iconRes = R.drawable.ic_heavysnow
    )
    object ModerateThunderstorm : ResWeatherType(
        weatherDesc = "Moderate thunderstorm",
        iconRes = R.drawable.ic_thunder
    )
    object SlightHailThunderstorm : ResWeatherType(
        weatherDesc = "Thunderstorm with slight hail",
        iconRes = R.drawable.ic_rainythunder
    )
    object HeavyHailThunderstorm : ResWeatherType(
        weatherDesc = "Thunderstorm with heavy hail",
        iconRes = R.drawable.ic_rainythunder
    )

    companion object {
        fun fromWeatherType(weatherType: WeatherType): ResWeatherType {
            return when (weatherType) {
                  WeatherType.ClearSkySun -> ClearSkySun

                  WeatherType.ClearSkyMoon -> ClearSkyMoon

                  WeatherType.MainlyClearSun -> ClearSkyMoon

                  WeatherType.MainlyClearMoon -> ClearSkyMoon

                  WeatherType.PartlyCloudySun -> ClearSkyMoon

                  WeatherType.PartlyCloudyMoon -> ClearSkyMoon

                  WeatherType.Overcast -> ClearSkyMoon

                  WeatherType.Foggy -> ClearSkyMoon

                  WeatherType.DepositingRimeFog -> ClearSkyMoon

                  WeatherType.LightDrizzle -> ClearSkyMoon

                  WeatherType.ModerateDrizzle -> ClearSkyMoon

                  WeatherType.DenseDrizzle -> ClearSkyMoon

                  WeatherType.LightFreezingDrizzle : WeatherType()

                  WeatherType.DenseFreezingDrizzle : WeatherType()

                  WeatherType.SlightRain : WeatherType()

                  WeatherType.ModerateRain : WeatherType()

                  WeatherType.HeavyRain : WeatherType()

                  WeatherType.HeavyFreezingRain : WeatherType()

                  WeatherType.SlightSnowFall : WeatherType()

                  WeatherType.ModerateSnowFall : WeatherType()

                  WeatherType.HeavySnowFall : WeatherType()

                  WeatherType.SnowGrains : WeatherType()

                  WeatherType.SlightRainShowers : WeatherType()

                  WeatherType.ModerateRainShowers : WeatherType()

                  WeatherType.ViolentRainShowers : WeatherType()

                  WeatherType.SlightSnowShowers : WeatherType()

                  WeatherType.HeavySnowShowers : WeatherType()

                  WeatherType.ModerateThunderstorm : WeatherType()

                  WeatherType.SlightHailThunderstorm : WeatherType()

                  WeatherType.HeavyHailThunderstorm : WeatherType()

            }
        }
    }
}
