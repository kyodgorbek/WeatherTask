package com.example.weathertask.ui

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathertask.databinding.FragmentCityBinding
import com.example.weathertask.model.Weather
import com.example.weathertask.ui.adapter.WeatherAdapter
import com.example.weathertask.viewmodels.MainViewModel
import javax.inject.Inject

class CityDetailsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @VisibleForTesting
    lateinit var viewModel: MainViewModel

    @Inject
    lateinit var adapter: WeatherAdapter
    private lateinit var binding: FragmentCityBinding

    override fun subscribeViewModel() {
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)
    }

    override fun observeValues() {
        viewModel.weathers.observe(viewLifecycleOwner, weathersObserver)
        viewModel.spinner.observe(this, spinnerObserver)
    }

    private val weathersObserver = Observer<List<Weather>?> { weathers ->
        adapter.submitList(weathers)
    }

    private val spinnerObserver = Observer<Boolean> { boolean ->
        binding.progressCircular.visibility = View.VISIBLE.takeIf { boolean } ?: View.GONE
    }


    override fun setSupportInjection(): Fragment {
        return this
    }

    override fun initViewBiding(): View {
        binding = FragmentCityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        with(binding) {
            weatherList.adapter = adapter
            cityName.text = viewModel.currentCity.name
            icBack.setOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
        viewModel.getWeather()
    }

    companion object {
        fun newInstance() = CityDetailsFragment().apply {
            arguments = bundleOf()
        }

        const val TAG = "CityDetailsFragment"
    }

}