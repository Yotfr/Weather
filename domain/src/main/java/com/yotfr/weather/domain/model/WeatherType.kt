package com.yotfr.weather.domain.model

/**
 * [WeatherType] contains all available types of weather and function which returns weather type
 * based on WMO code
 */
sealed class WeatherType {
    object ClearSkySun : WeatherType()
    object ClearSkyMoon : WeatherType()
    object MainlyClearSun : WeatherType()
    object MainlyClearMoon : WeatherType()
    object PartlyCloudySun : WeatherType()
    object PartlyCloudyMoon : WeatherType()
    object Overcast : WeatherType()
    object Foggy : WeatherType()
    object DepositingRimeFog : WeatherType()
    object LightDrizzle : WeatherType()
    object ModerateDrizzle : WeatherType()
    object DenseDrizzle : WeatherType()
    object LightFreezingDrizzle : WeatherType()
    object DenseFreezingDrizzle : WeatherType()
    object SlightRain : WeatherType()
    object ModerateRain : WeatherType()
    object HeavyRain : WeatherType()
    object HeavyFreezingRain : WeatherType()
    object SlightSnowFall : WeatherType()
    object ModerateSnowFall : WeatherType()
    object HeavySnowFall : WeatherType()
    object SnowGrains : WeatherType()
    object SlightRainShowers : WeatherType()
    object ModerateRainShowers : WeatherType()
    object ViolentRainShowers : WeatherType()
    object SlightSnowShowers : WeatherType()
    object HeavySnowShowers : WeatherType()
    object ModerateThunderstorm : WeatherType()
    object SlightHailThunderstorm : WeatherType()
    object HeavyHailThunderstorm : WeatherType()

    companion object {
        fun fromWMO(code: Int, isDayTime: Boolean): WeatherType {
            return when (code) {
                0 -> if (isDayTime) ClearSkySun else ClearSkyMoon
                1 -> if (isDayTime) MainlyClearSun else MainlyClearMoon
                2 -> if (isDayTime) PartlyCloudySun else PartlyCloudyMoon
                3 -> Overcast
                45 -> Foggy
                48 -> DepositingRimeFog
                51 -> LightDrizzle
                53 -> ModerateDrizzle
                55 -> DenseDrizzle
                56 -> LightFreezingDrizzle
                57 -> DenseFreezingDrizzle
                61 -> SlightRain
                63 -> ModerateRain
                65 -> HeavyRain
                66 -> LightFreezingDrizzle
                67 -> HeavyFreezingRain
                71 -> SlightSnowFall
                73 -> ModerateSnowFall
                75 -> HeavySnowFall
                77 -> SnowGrains
                80 -> SlightRainShowers
                81 -> ModerateRainShowers
                82 -> ViolentRainShowers
                85 -> SlightSnowShowers
                86 -> HeavySnowShowers
                95 -> ModerateThunderstorm
                96 -> SlightHailThunderstorm
                99 -> HeavyHailThunderstorm
                else -> ClearSkySun
            }
        }
    }
}
