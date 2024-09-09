package com.example.weatherapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.Models.DataModel
import com.example.weatherapp.Models.DefaultWeatherModel
import com.example.weatherapp.Models.WeatherModel
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.databinding.FragmentDaysBinding

class DaysFragment : Fragment(), WeatherAdapter.Listner {
    private lateinit var binding: FragmentDaysBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.liveDataWeather.observe(viewLifecycleOwner) {
            adapter.submitList(getDaysList(it.forecast.forecastday))
        }
    }

    private fun init() = with(binding) {
        adapter = WeatherAdapter(this@DaysFragment)
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
    }

    private fun getDaysList(daysArray: List<DataModel>): List<DefaultWeatherModel> {
        val list = ArrayList<DefaultWeatherModel>()
        for (i in 0 until daysArray.size) {
            val item = DefaultWeatherModel(
                i,
                daysArray[i].date,
                daysArray[i].day.condition.text,
                "",
                daysArray[i].day.maxtemp_c.toString(),
                daysArray[i].day.mintemp_c.toString(),
                daysArray[i].day.condition.icon
            )
            list.add(item)
        }
        return list
    }


    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    override fun onClick(item: DefaultWeatherModel) {
        model.liveDataWeather.value = WeatherModel(
            item.id,
            model.liveDataWeather.value!!.location,
            model.liveDataWeather.value!!.current,
            model.liveDataWeather.value!!.forecast,
        )
    }

}