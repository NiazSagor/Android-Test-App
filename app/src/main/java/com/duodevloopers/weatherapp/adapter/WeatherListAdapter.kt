package com.duodevloopers.weatherapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.duodevloopers.weatherapp.clicklistener.OnClickListener
import com.duodevloopers.weatherapp.databinding.ListItemBinding
import com.duodevloopers.weatherapp.model.WeatherData
import com.duodevloopers.weatherapp.util.StringUtility

class WeatherListAdapter(private val list: List<WeatherData>) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    private lateinit var onClickListener: OnClickListener

    inner class WeatherViewHolder(
        private val binding: ListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherData: WeatherData) {
            binding.apply {
                city.text = weatherData.name
                condition.text = weatherData.weather[0].main
                temp.text = StringUtility.toCelsius(weatherData.main.temp)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(list[position])

        holder.itemView.setOnClickListener {
            onClickListener.onClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }



    fun setOnClickListener (onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


}