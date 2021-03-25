package com.jg.astroweather.presentation.weather

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.jg.astroweather.R
import com.jg.astroweather.common.SafeFunctionExecutorImpl
import com.jg.astroweather.data.datasources.remote.RemoteDataSource
import com.jg.astroweather.data.repositories.WeatherRepositoryImpl
import com.jg.astroweather.databinding.FragmentWeatherBinding
import com.jg.astroweather.domain.entities.CurrentWeather
import com.jg.astroweather.domain.errors.ErrorHandlerImpl
import com.jg.astroweather.presentation.BaseFragment
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

class WeatherFragment: BaseFragment() {

    private lateinit var binding: FragmentWeatherBinding
    private lateinit var viewModel: WeatherViewModel

    private var addresses = mutableListOf<Address>()
    private lateinit var geocoder: Geocoder
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationPermissionCode = 2
    private var lon = 0.0
    private var lat = 0.0
    private var city = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherRepositoryImpl = WeatherRepositoryImpl(RemoteDataSource())
        val safeFunctionExecutor = SafeFunctionExecutorImpl(ErrorHandlerImpl())
        val viewModelFactory = WeatherViewModelFactory(weatherRepositoryImpl,  safeFunctionExecutor)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        geocoder = Geocoder(context, Locale.getDefault())
        viewModel = ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
        viewModel.init()
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect(::handleState)
        }


        arguments?.let {
            val cityId = WeatherFragmentArgs.fromBundle(it).cityId
            if(cityId == 0){
                getLocationFromGPS()
            }else {
                viewModel.onSearchButtonTapped(cityId)
            }
        }
    }

    private fun handleState(state: WeatherUIState){
        when(state){
            WeatherUIState.Loading -> displayLoadingState()
            is WeatherUIState.Success -> displayWeatherState(state.currentWeather)
            WeatherUIState.Initial -> displayInitialState()
            is WeatherUIState.Error -> displayErrorState(state.errorMessage)
        }
    }

    private fun displayErrorState(errorMessage: String) {
        binding.progressBar.visibility = View.GONE
        binding.descLinearLayout.visibility = View.GONE
        showErrorMessage(errorMessage)
    }

    private fun displayInitialState() {
        binding.progressBar.visibility = View.GONE
    }

    private fun displayWeatherState(currentWeather: CurrentWeather) {
        Log.d(WeatherFragment::class.java.simpleName, currentWeather.toString())
        binding.progressBar.visibility = View.GONE
        binding.descLinearLayout.visibility = View.VISIBLE
        binding.cityTextView.text = currentWeather.name
        if (!currentWeather.weather.isNullOrEmpty()){
            Glide.with(this)
                    .load(currentWeather.weather[0].iconUrl)
                    .centerCrop()
                    .into(binding.imageViewWeather);
            binding.imageViewWeather.visibility = View.VISIBLE

        }else{
            binding.imageViewWeather.visibility = View.GONE
        }
        binding.tempTextView.text = currentWeather.main?.temp.toString().plus(" C°")
        binding.humTextView.text = getString(R.string.humedity) + currentWeather.main?.humidity.toString()
        binding.pressTextView.text = getString(R.string.pressure) + currentWeather.main?.pressure.toString()
        binding.sunriseTextView.text = getString(R.string.sunrise) + currentWeather.sys?.sunrise.toString()
        binding.sunsetTextView.text = getString(R.string.sunset) + currentWeather.sys?.sunset.toString()
        binding.visTextView.text = getString(R.string.visibility) + currentWeather.visibility.toString()
        binding.windTextView.text = getString(R.string.wind) +  currentWeather.wind?.speed.toString()

    }

    private fun displayLoadingState() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun getLocationFromGPS() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
            )
            return
        }
        viewModel.gettingCurrentLocation()
        fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY,
                null
        ).addOnSuccessListener {
            if (it != null) {
                var newCity = ""
                try {
                    addresses = geocoder.getFromLocation(
                            it.latitude,
                            it.longitude,
                            1
                    )
                    newCity = if (addresses[0].locality.isNullOrEmpty()) {
                        addresses[0].adminArea
                    } else {
                        addresses[0].locality
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if ((roundDouble(it.latitude) != lat && roundDouble(it.longitude) != lon) || newCity != city) {
                    lat = roundDouble(it.latitude)
                    lon = roundDouble(it.longitude)
                    city = newCity
                    viewModel.onSearchWeatherLocation(lat, lon)

                }
            } else {
                viewModel.onError("Failed to get Location")
            }
        }.addOnFailureListener {
            viewModel.onError("Failed to get Location")
        }
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocationFromGPS()

            } else {
                viewModel.onError("Failed to get Location")
            }
        }

    }

    private fun roundDouble(double: Double): Double {
        return BigDecimal(double).setScale(4, RoundingMode.HALF_UP).toDouble()
    }

}