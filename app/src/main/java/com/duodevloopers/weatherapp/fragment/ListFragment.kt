package com.duodevloopers.weatherapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.duodevloopers.weatherapp.R
import com.duodevloopers.weatherapp.adapter.WeatherListAdapter
import com.duodevloopers.weatherapp.clicklistener.OnClickListener
import com.duodevloopers.weatherapp.databinding.FragmentListBinding
import com.duodevloopers.weatherapp.model.WeatherData
import com.duodevloopers.weatherapp.viewmodel.MainActivityViewModel

class ListFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentListBinding

    private lateinit var mainActivityViewModel: MainActivityViewModel

    private lateinit var adapter: WeatherListAdapter

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
        binding = FragmentListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainActivityViewModel.getInitialCityWeather().observe(viewLifecycleOwner, Observer {
            adapter = WeatherListAdapter(it.list)
            binding.list.adapter = adapter
            adapter.setOnClickListener(this)
        })
    }

    override fun onClick(weatherData: WeatherData) {
        mainActivityViewModel.setSelectedCity(weatherData)
        findNavController().navigate(R.id.action_listFragment_to_mapFragment)
    }
}