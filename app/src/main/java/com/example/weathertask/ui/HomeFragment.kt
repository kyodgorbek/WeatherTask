package com.example.weathertask.ui

import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weathertask.R
import com.example.weathertask.databinding.FragmentHomeBinding

import com.example.weathertask.model.City
import com.example.weathertask.model.CityUiModel
import com.example.weathertask.ui.adapter.CityAdapter
import com.example.weathertask.ui.adapter.CityListener
import com.example.weathertask.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import javax.inject.Inject

class HomeFragment : BaseFragment(), CityListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var adapter: CityAdapter
    private lateinit var binding: FragmentHomeBinding

    @VisibleForTesting
    lateinit var viewModel: MainViewModel
    override fun subscribeViewModel() {
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)
            .get(MainViewModel::class.java).apply {
                getCities()
            }
    }

    override fun observeValues() {
        viewModel.cities.observe(viewLifecycleOwner, citiesObserver)
        viewModel.snackbar.observe(viewLifecycleOwner, snackBarObserver)
    }

    private val citiesObserver = Observer<List<City>?> { cities ->
        adapter.submitList(cities?.map { CityUiModel(it) })
    }

    private val snackBarObserver = Observer<Int?> { text ->
        text?.let {
            Snackbar.make(binding.container, text, Snackbar.LENGTH_SHORT)
                .show()
            viewModel.onSnackbarShown()
        }
    }

    override fun setSupportInjection(): Fragment {
        return this
    }

    override fun initViewBiding(): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews() {
        adapter.listener = this
        viewModel.setDate(calendarView.date)
        with(binding) {
            citiesList.adapter = adapter
            calendarView.setOnDateChangeListener { calendarView, year, month, day ->
                val c: Calendar = Calendar.getInstance()
                c.set(year, month, day)
                viewModel.currentDate = c.time
            }
            validateButton.setOnClickListener {
                if (viewModel.isValid()) {
                    redirectToCityDetails()
                }
            }
        }
    }

    private fun redirectToCityDetails() {
        val fragment = parentFragmentManager.findFragmentByTag(CityDetailsFragment.TAG)
            ?: CityDetailsFragment.newInstance()
        parentFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment, CityDetailsFragment.TAG)
            addToBackStack(null)
            commit()
        }
    }


    override fun onClick(city: City) {
        adapter.submitList(viewModel.updateCitySelected(city))
    }
}