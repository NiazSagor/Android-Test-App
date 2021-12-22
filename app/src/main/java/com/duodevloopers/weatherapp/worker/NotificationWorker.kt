package com.duodevloopers.weatherapp.worker

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.duodevloopers.weatherapp.R
import com.duodevloopers.weatherapp.api.RetrofitInstance
import com.duodevloopers.weatherapp.model.WeatherInfo
import com.duodevloopers.weatherapp.myapp.MyApp
import com.duodevloopers.weatherapp.util.StringUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

class NotificationWorker(
    context: Context, params: WorkerParameters
) : Worker(context, params) {

    @SuppressLint("MissingPermission")
    override fun doWork(): Result {

        Log.d("worker", "worker working")

        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(applicationContext)

        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {

                Log.d("worker", "location" + it.latitude + it.longitude)

                GlobalScope.launch {
                    val response: Response<WeatherInfo> = RetrofitInstance.api.getWeather(
                        String.format("%.2f", it.latitude),
                        String.format("%.2f", it.longitude),
                        "50",
                        MyApp.appId
                    )
                    if (response.isSuccessful) response.body()?.list?.get(0)?.main?.let { it1 ->
                        sendNotification(
                            it1.temp
                        )
                    }
                }

            } else {
                Log.d("worker", "location is null")
            }

        }

        return Result.retry()

    }

    private fun sendNotification(temp: Double) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, "push-notification-channel-id")
                .setContentTitle("WeatherApp")
                .setContentText("Current temperature: " + String.format("%s", StringUtility.toCelsius(temp)))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setAutoCancel(true)

        notificationManager.notify((0..999).random(), notification.build())
    }
}