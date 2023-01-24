package com.yotfr.weather

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.yotfr.weather.domain.model.FavoritePlaceInfo
import com.yotfr.weather.domain.usecases.GetWeatherInfoForFavoritePlace
import com.yotfr.weather.domain.util.Response
import com.yotfr.weather.presentation.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class WeatherWidgetProvider : AppWidgetProvider() {

    @Inject
    lateinit var getWeatherInfoForFavoritePlace: GetWeatherInfoForFavoritePlace

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            val pendingIntent: PendingIntent = PendingIntent.getActivity(
                /* context = */ context,
                /* requestCode = */ 0,
                /* intent = */ Intent(context, MainActivity::class.java),
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            // Get the layout for the widget and attach an on-click listener
            // to the button.
            var placeInfo = FavoritePlaceInfo(
                0,
                "Unknown",
                30.4,
                30.90,
                "Unknown",
                "Unknown",
                weatherInfo = null
            )
            goAsync {
                getWeatherInfoForFavoritePlace(-2L).collectLatest {
                    when (it) {
                        is Response.Loading -> {
                            if (it.data != null) {
                                placeInfo = it.data
                            }
                        }
                        is Response.Success -> {
                            if (it.data != null) {
                                placeInfo = it.data
                            }
                        }
                        else -> {
                            placeInfo = FavoritePlaceInfo(
                                0,
                                "ERRR",
                                30.4,
                                30.90,
                                "ERRR",
                                "ERRR",
                                weatherInfo = null
                            )
                        }
                    }
                }
                delay(4000)
            }



            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.weather_widget
            ).apply {
                this.setTextViewText(R.id.widget_place_name, placeInfo.placeName)
                this.setTextViewText(R.id.widget_current_temperature, placeInfo.weatherInfo?.currentWeatherData?.temperature.toString())
                this.setImageViewResource(
                    R.id.widget_weather_type,
                    placeInfo.weatherInfo?.currentWeatherData?.weatherType?.iconRes ?: R.drawable.ic_heavyrainy
                )
                setOnClickPendingIntent(this.layoutId, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current
            // widget.
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
