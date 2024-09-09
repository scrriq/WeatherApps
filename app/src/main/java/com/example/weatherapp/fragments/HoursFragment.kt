package com.example.weatherapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.Models.DefaultWeatherModel
import com.example.weatherapp.Models.HourModel
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initRcView()
//        fillDedaultModel()
        model.liveDataWeather.observe(viewLifecycleOwner) {
            adapter.submitList(getHoursList(it.forecast.forecastday[it.currentDay].hour))
        }
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter(null)
        rcView.adapter = adapter

    }


    private fun getHoursList(hoursArray: List<HourModel>): List<DefaultWeatherModel> {
        val list = ArrayList<DefaultWeatherModel>()
        for (i in 0 until hoursArray.size) {
            val item = DefaultWeatherModel(
                0,
                hoursArray[i].time,
                hoursArray[i].condition.text,
                hoursArray[i].temp_c.toString(),
                "",
                "",
                hoursArray[i].condition.icon
            )

            list.add(item)
        }
        return list
    }


    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}