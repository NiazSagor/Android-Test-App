package com.duodevloopers.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.duodevloopers.weatherapp.R
import com.duodevloopers.weatherapp.databinding.FragmentMapBinding
import com.duodevloopers.weatherapp.util.StringUtility
import com.duodevloopers.weatherapp.viewmodel.MainActivityViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapBinding
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainActivityViewModel =
            ViewModelProvider(requireActivity()).get(MainActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMapBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?

        map?.getMapAsync(this)

        val weatherData = mainActivityViewModel.getSelectedCity()

        binding.name.text = weatherData.name
        binding.condition.text = weatherData.weather[0].description
        binding.humidity.text = String.format("Humidity: %d%s",weatherData.main.humidity, "%")
        binding.windSpeed.text = String.format("Wind Speed: %.2f",weatherData.wind.speed)
        binding.maxTemp.text = String.format("Max. Temp: %s",StringUtility.toCelsius(weatherData.main.temp_max))
        binding.minTemp.text = String.format("Min. Temp: %s",StringUtility.toCelsius(weatherData.main.temp_min))
        binding.temp.text = StringUtility.toCelsius(weatherData.main.temp)
    }

    override fun onMapReady(p0: GoogleMap) {
        val weatherData = mainActivityViewModel.getSelectedCity()
        val latLng = LatLng(weatherData.coord.lat, weatherData.coord.lon)
        p0.addMarker(MarkerOptions().position(latLng).title(weatherData.name))
        p0.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
    }
}