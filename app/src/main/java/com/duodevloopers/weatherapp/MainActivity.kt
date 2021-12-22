package com.duodevloopers.weatherapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.*
import com.duodevloopers.weatherapp.worker.NotificationWorker
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (isGPSEnabled()) {
            start()
        } else {
            // request permission
            getCurrentLocation()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    if (isGPSEnabled()) getCurrentLocation()
                    Toast.makeText(this, "Location permission is granted", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Location permission is needed", Toast.LENGTH_LONG).show()
                }
                return
            }
            101 -> {
                start()
                Toast.makeText(this, "GPS is enabled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isGPSEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        val providerEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (providerEnable) {
            return true
        } else {
            AlertDialog.Builder(this).setTitle("GPS Permission")
                .setMessage("Please Enable GPS")
                .setPositiveButton("OK") { dialogInterface: DialogInterface?, i: Int ->
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivityForResult(
                        intent,
                        101
                    )
                }.setCancelable(true).show()

        }
        return false
    }

    private fun getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ), 100
            )

            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
//                Toast.makeText(this, "success" + it.latitude + it.longitude, Toast.LENGTH_LONG)
//                    .show()
            } else {
//                Toast.makeText(this, "location is null", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener {
//            Toast.makeText(this, "failed" + it.message, Toast.LENGTH_LONG).show()
        }.addOnCanceledListener {
//            Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show()
        }
    }

    private fun start() {

        val constraints = Constraints.Builder().setRequiresCharging(false)
            .setRequiredNetworkType(NetworkType.UNMETERED).build()

        val periodicWorkRequest =
            PeriodicWorkRequest.Builder(NotificationWorker::class.java, 1, TimeUnit.DAYS)
                .setInputData(Data.Builder().putBoolean("isStart", true).build())
                .setInitialDelay(3000, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .build()

        val workManager = WorkManager.getInstance(this)

        workManager.enqueue(periodicWorkRequest)

        workManager.getWorkInfoByIdLiveData(periodicWorkRequest.id).observeForever {
            if (it != null) {

                Log.d("periodicWorkRequest", "Status changed to ${it.state.isFinished}")

            }
        }
    }
}