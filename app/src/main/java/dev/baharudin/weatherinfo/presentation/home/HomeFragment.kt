package dev.baharudin.weatherinfo.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.baharudin.weatherinfo.R
import dev.baharudin.weatherinfo.databinding.FragmentHomeBinding
import dev.baharudin.weatherinfo.domain.entities.Condition
import dev.baharudin.weatherinfo.domain.entities.Location
import dev.baharudin.weatherinfo.presentation.search_location.SearchLocationAdapter


@AndroidEntryPoint
class HomeFragment : Fragment() {

    companion object {
        private const val TAG = "HomeFragment"
    }

    private lateinit var binding: FragmentHomeBinding

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val searchLocationViewModel: SearchLocationViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val activity = activity as AppCompatActivity?

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    if (binding.searchView.isShowing) {
                        binding.searchView.hide()
                        return
                    }

                    activity?.finish()
                }
            })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

        searchLocationViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                showSearchLocationList(state.data)
            }

            if (state.message.isNotBlank()) {
                Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupUi() {
        setupAppBar()
        setupSavedLocationUi()
        setupSearchView()
    }

    private fun setupSavedLocationUi() {
        with(binding) {
            rvSavedLocation.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(requireContext())
            rvSavedLocation.layoutManager = layoutManager
            rvSavedLocation.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    layoutManager.orientation
                )
            )
            showSavedLocationList()
        }

        homeViewModel.savedConditionList.observe(viewLifecycleOwner) { state ->
            showSavedLocationList(state.data)
        }
    }

    private fun setupAppBar() {
        with(binding) {
            topAppbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.btn_add -> {
                        searchView.show()
                    }
                }
                true
            }
        }
    }

    private fun setupSearchView() {
        with(binding) {
            searchView.editText.addTextChangedListener {
                if (it != null)
                    searchLocationViewModel.search(it.toString())
            }

            rvSearchLocation.setHasFixedSize(true)
            val layoutManager = LinearLayoutManager(requireContext())
            rvSearchLocation.layoutManager = layoutManager
            rvSearchLocation.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    layoutManager.orientation
                )
            )
            showSearchLocationList()
        }
    }

    private fun showSavedLocationList(conditions: ArrayList<Condition>? = null) {
        val adapter =
            SavedLocationAdapter(requireContext(), conditions ?: arrayListOf()) { condition ->
                val toConditionDetailFragment =
                    HomeFragmentDirections.actionHomeFragmentToConditionDetailFragment(condition)
                view?.findNavController()?.navigate(toConditionDetailFragment)
            }
        binding.rvSavedLocation.adapter = adapter
    }

    private fun showSearchLocationList(locations: ArrayList<Location> = arrayListOf()) {
        val adapter = SearchLocationAdapter(locations) { location ->
            val toConditionDetailFragment =
                HomeFragmentDirections.actionHomeFragmentToConditionDetailFragment(
                    Condition(
                        location = location
                    )
                )
            view?.findNavController()?.navigate(toConditionDetailFragment)
        }
        binding.rvSearchLocation.adapter = adapter
    }
}