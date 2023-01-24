package com.yotfr.weather.presentation.utils

import com.yotfr.weather.R
import com.yotfr.weather.domain.model.WeatherType

fun WeatherType.getIconRes(): Int {
    return when (this) {
        WeatherType.ClearSkySun -> R.drawable.ic_clear_sky_sun
        WeatherType.ClearSkyMoon -> R.drawable.ic_clear_sky_moon
        WeatherType.MainlyClearSun -> R.drawable.ic_mainly_clear_sun
        WeatherType.MainlyClearMoon -> R.drawable.ic_mainly_clear_moon
        WeatherType.PartlyCloudySun -> R.drawable.ic_partly_cloudy_sun
        WeatherType.PartlyCloudyMoon -> R.drawable.ic_partly_cloudy_moon
        WeatherType.Overcast -> R.drawable.ic_cloudy
        WeatherType.Foggy -> R.drawable.ic_foggy
        WeatherType.DepositingRimeFog -> R.drawable.ic_foggy
        WeatherType.LightDrizzle -> R.drawable.ic_drizzle
        WeatherType.ModerateDrizzle -> R.drawable.ic_drizzle
        WeatherType.DenseDrizzle -> R.drawable.ic_drizzle
        WeatherType.LightFreezingDrizzle -> R.drawable.ic_sleet
        WeatherType.DenseFreezingDrizzle -> R.drawable.ic_sleet
        WeatherType.SlightRain -> R.drawable.ic_rainy
        WeatherType.ModerateRain -> R.drawable.ic_rainy
        WeatherType.HeavyRain -> R.drawable.ic_heavyrainy
        WeatherType.HeavyFreezingRain -> R.drawable.ic_sleet
        WeatherType.SlightSnowFall -> R.drawable.ic_snowy
        WeatherType.ModerateSnowFall -> R.drawable.ic_snowy
        WeatherType.HeavySnowFall -> R.drawable.ic_snowy
        WeatherType.SnowGrains -> R.drawable.ic_hail
        WeatherType.SlightRainShowers -> R.drawable.ic_rainy
        WeatherType.ModerateRainShowers -> R.drawable.ic_rainy
        WeatherType.ViolentRainShowers -> R.drawable.ic_heavyrainy
        WeatherType.SlightSnowShowers -> R.drawable.ic_heavysnow
        WeatherType.HeavySnowShowers -> R.drawable.ic_heavysnow
        WeatherType.ModerateThunderstorm -> R.drawable.ic_thunder
        WeatherType.SlightHailThunderstorm -> R.drawable.ic_rainythunder
        WeatherType.HeavyHailThunderstorm -> R.drawable.ic_rainythunder
    }
}

fun WeatherType.getWeatherDescStringRes(): Int {
    return when (this) {
        WeatherType.ClearSkySun -> R.string.weather_desc_clear_sky
        WeatherType.ClearSkyMoon -> R.string.weather_desc_clear_sky
        WeatherType.MainlyClearSun -> R.string.weather_desc_mainly_clear
        WeatherType.MainlyClearMoon -> R.string.weather_desc_mainly_clear
        WeatherType.PartlyCloudySun -> R.string.weather_desc_partly_cloudy
        WeatherType.PartlyCloudyMoon -> R.string.weather_desc_partly_cloudy
        WeatherType.Overcast -> R.string.weather_desc_overcast
        WeatherType.Foggy -> R.string.weather_desc_foggy
        WeatherType.DepositingRimeFog -> R.string.weather_desc_depositing_rime_fog
        WeatherType.LightDrizzle -> R.string.weather_desc_clear_light_drizzle
        WeatherType.ModerateDrizzle -> R.string.weather_desc_clear_moderate_drizzle
        WeatherType.DenseDrizzle -> R.string.weather_desc_clear_dense_drizzle
        WeatherType.LightFreezingDrizzle -> R.string.weather_desc_clear_slight_freezing_drizzle
        WeatherType.DenseFreezingDrizzle -> R.string.weather_desc_clear_dense_freezing_drizzle
        WeatherType.SlightRain -> R.string.weather_desc_clear_slight_rain
        WeatherType.ModerateRain -> R.string.weather_desc_clear_rainy
        WeatherType.HeavyRain -> R.string.weather_desc_clear_heavy_rain
        WeatherType.HeavyFreezingRain -> R.string.weather_desc_clear_heavy_freezing_rain
        WeatherType.SlightSnowFall -> R.string.weather_desc_clear_slight_snow_fall
        WeatherType.ModerateSnowFall -> R.string.weather_desc_clear_moderate_snow_fall
        WeatherType.HeavySnowFall -> R.string.weather_desc_clear_heavy_snow_fall
        WeatherType.SnowGrains -> R.string.weather_desc_light_snow_grains
        WeatherType.SlightRainShowers -> R.string.weather_desc_slight_rain_showers
        WeatherType.ModerateRainShowers -> R.string.weather_desc_moderate_rain_showers
        WeatherType.ViolentRainShowers -> R.string.weather_desc_violent_rain_showers
        WeatherType.SlightSnowShowers -> R.string.weather_desc_light_snow_showers
        WeatherType.HeavySnowShowers -> R.string.weather_desc_heavy_snow_showers
        WeatherType.ModerateThunderstorm -> R.string.weather_desc_moderate_thunderstorm
        WeatherType.SlightHailThunderstorm -> R.string.weather_desc_clear_thunderstorm_with_slight_hail
        WeatherType.HeavyHailThunderstorm -> R.string.weather_desc_clear_thunderstorm_with_heavy_hail
    }
}
