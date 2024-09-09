package com.example.weatherapp.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.airbnb.lottie.LottieDrawable
import com.example.weatherapp.DialogManager
import com.example.weatherapp.MainApi
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.Models.DefaultWeatherModel
import com.example.weatherapp.adapters.VpAdapter
import com.example.weatherapp.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

const val API_KEY = "4e5928bef0154d68a4f160924240307"

@AndroidEntryPoint
class MainFragment : Fragment() {
    @Inject
    lateinit var mainApi: MainApi
    private lateinit var fLocationClient: FusedLocationProviderClient
    private val fList = listOf(
        HoursFragment.newInstance(), DaysFragment.newInstance()
    )
    private val tList = listOf(
        "Hours", "Days"
    )
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnimation()
        checkPermission()
        init()
        updateCurrentCard()
    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }

    private fun init() = with(binding) {
        fLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val adapter = VpAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp) { tab, pos ->
            tab.text = tList[pos]
        }.attach()
        ibSync.setOnClickListener {
            tabLayout.selectTab(tabLayout.getTabAt(0))
            checkLocation()
        }
        ibSearch.setOnClickListener {
            ibSearch.visibility = View.GONE
            ibSync.visibility = View.GONE
            initSearchView()
        }

    }

    private fun requestWeatherData(city: String) {
        model.getWeatherForecast(
            API_KEY,
            city,
            "3",
            "no",
            "no"
        )
    }

    private fun checkLocation() {
        if (isLocationEnabled()) {
            getLocation()
        } else {
            DialogManager.locationSettingsDialog(requireContext(), object : DialogManager.Listner {
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
            })
        }
    } // Запрос на включение локации, в случае если она выключена

    private fun isLocationEnabled(): Boolean {
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    } // Проверка на влкючение локации

    private fun getLocation() {
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                requestWeatherData("${it.result.latitude}, ${it.result.longitude}") // reqWeatherData
            }
    } // получение локации по GPS


    private fun updateCurrentCard() = with(binding) {
        model.liveDataWeather.observe(viewLifecycleOwner) {
            val maxMinTemp =
                "${it.forecast.forecastday[it.currentDay].day.maxtemp_c.toInt()}°С / ${it.forecast.forecastday[it.currentDay].day.mintemp_c.toInt()}°С"
            tvData.text = it.current.last_updated
            tvCity.text = it.location.name
            tvCurrentTemp.text = if(it.currentDay == 0) it.current.temp_c.toString() else maxMinTemp
            tvCondition.text = if(it.currentDay == 0) it.current.condition.text else it.forecast.forecastday[it.currentDay].day.condition.text
            tvMaxMin.text = if (it.currentDay != 0) "" else maxMinTemp
            Picasso.get().load(
                "https:" +
                        if(it.currentDay == 0) it.current.condition.icon
                        else it.forecast.forecastday[it.currentDay].day.condition.icon)
                .into(imWeather)

            binding.WaitingAnimation.pauseAnimation()
            binding.WaitingAnimation.visibility = View.GONE
            tabLayout.selectTab(tabLayout.getTabAt(0))
        }
    } // обсервер на обравление текущей карты

    private fun permissionListener() {
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    } // проверка на permission


    private fun initAnimation() = with(binding) {
        WaitingAnimation.setMinProgress(0.0f)
        WaitingAnimation.setMaxProgress(1.0f)
        WaitingAnimation.repeatCount = LottieDrawable.INFINITE
        WaitingAnimation.repeatMode = LottieDrawable.RESTART
        WaitingAnimation.playAnimation()
    } // анимация ожидания


    private fun initSearchView() = with(binding) {
        searchWeatherView.isIconified = false
        searchWeatherView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                requestWeatherData(query.toString())
                searchWeatherView.onActionViewCollapsed()
                ibSearch.visibility = View.VISIBLE
                ibSync.visibility = View.VISIBLE
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        }
        )
    }// поиск погоды по городам


    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}