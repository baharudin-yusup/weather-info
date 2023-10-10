package dev.baharudin.weatherinfo.presentation.views

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.AndroidEntryPoint
import dev.baharudin.weatherinfo.R
import dev.baharudin.weatherinfo.databinding.FragmentConditionDetailBinding
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.presentation.common.getWeatherIconUrl
import dev.baharudin.weatherinfo.presentation.views.adapter.DailyForecastConditionAdapter
import dev.baharudin.weatherinfo.presentation.views.adapter.HourlyForecastConditionAdapter
import dev.baharudin.weatherinfo.presentation.viewmodels.ConditionDetailViewModel
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class ConditionDetailFragment : Fragment() {
    companion object {
        private const val TAG = "ConditionDetailFragment"
    }

    private val args: ConditionDetailFragmentArgs by navArgs()

    @Inject
    lateinit var factory: ConditionDetailViewModel.Factory

    private val conditionDetailViewModel: ConditionDetailViewModel by viewModels {
        ConditionDetailViewModel.providesFactory(factory, args.condition)
    }

    private lateinit var binding: FragmentConditionDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConditionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
    }

    private fun setupUi() {
        setupTopAppbarUi()
        setupCurrentConditionUi()
        setupHourlyForecastConditionUi()
        setupDailyForecastConditionUi()
    }

    private fun setupTopAppbarUi() {
        binding.appbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.btn_save_location -> {
                    conditionDetailViewModel.onSaveButtonClick()
                }
            }
            true
        }

        binding.appbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        conditionDetailViewModel.isLocationSaved.observe(viewLifecycleOwner) { state ->
            binding.appbar.menu.findItem(R.id.btn_save_location).apply {
                isVisible = state.data != null
                if (state.data != null && state.data) {
                    setIcon(R.drawable.ic_bookmark_on_24dp)
                } else {
                    setIcon(R.drawable.ic_bookmark_off_24dp)
                }
            }

            if (state.message.isNotBlank()) {
                Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCurrentConditionUi() {
        conditionDetailViewModel.currentConditionState.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                val (city, countryState, country) = state.data.location
                binding.appbar.title = city

                val subtitle = if (countryState.isBlank()) Locale(
                    "",
                    country
                ).displayCountry else "$countryState, ${Locale("", country).displayCountry}"

                binding.appbar.subtitle = subtitle
                Glide.with(this).load(state.data.getWeatherIconUrl())
                    .into(
                        object : CustomTarget<Drawable>(120, 120) {
                            override fun onLoadCleared(placeholder: Drawable?) {
                                binding.tvCurrentTemperature.setCompoundDrawablesWithIntrinsicBounds(
                                    placeholder,
                                    null,
                                    null,
                                    null
                                )
                            }

                            override fun onResourceReady(
                                resource: Drawable,
                                transition: Transition<in Drawable>?
                            ) {
                                binding.tvCurrentTemperature.setCompoundDrawablesWithIntrinsicBounds(
                                    resource,
                                    null,
                                    null,
                                    null
                                )
                            }
                        }
                    )

                val currentTemperature = state.data.temperature?.current
                if (currentTemperature != null)
                    binding.tvCurrentTemperature.text =
                        getString(R.string.temperature, currentTemperature)
            }

            if (state.message.isNotBlank()) {
                Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupHourlyForecastConditionUi() {
        with(binding) {
            rvHourlyForecast.setHasFixedSize(true)
            val layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvHourlyForecast.layoutManager = layoutManager
            showHourlyForecastConditionList()
        }

        conditionDetailViewModel.hourlyForecastConditionState.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                binding.pbHourlyForecast.visibility = View.GONE
                showHourlyForecastConditionList(state.data)
            }

            if (state.message.isNotBlank()) {
                showToast(state.message)
            }
        }
    }

    private fun setupDailyForecastConditionUi() {
        with(binding) {
            rvDailyForecast.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(requireContext())
            rvDailyForecast.layoutManager = layoutManager
            showDailyForecastConditionList()
        }

        conditionDetailViewModel.dailyForecastConditionState.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                binding.pbDailyForecast.visibility = View.GONE
                showDailyForecastConditionList(state.data)
            }

            if (state.message.isNotBlank()) {
                showToast(state.message)
            }
        }
    }


    private fun showHourlyForecastConditionList(conditions: ArrayList<Condition> = arrayListOf()) {
        val adapter =
            HourlyForecastConditionAdapter(requireContext(), conditions)
        binding.rvHourlyForecast.adapter = adapter
    }

    private fun showDailyForecastConditionList(conditions: ArrayList<Condition> = arrayListOf()) {
        val adapter =
            DailyForecastConditionAdapter(requireContext(), conditions)
        binding.rvDailyForecast.adapter = adapter
    }


    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
}
