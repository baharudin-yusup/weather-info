package dev.baharudin.weatherinfo.presentation.views

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import dev.baharudin.weatherinfo.presentation.viewmodels.HomeViewModel
import dev.baharudin.weatherinfo.presentation.viewmodels.SearchLocationViewModel
import dev.baharudin.weatherinfo.presentation.views.adapter.SavedLocationAdapter
import dev.baharudin.weatherinfo.presentation.views.adapter.SearchLocationAdapter


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
        modifyOnBackPressed()
        return binding.root
    }

    private fun modifyOnBackPressed() {
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()

    }

    private fun setupUi() {
        setupAppBarUi()
        setupSavedLocationUi()
        setupSearchLocationUi()
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

    private fun setupAppBarUi() {
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

    private fun setupSearchLocationUi() {
        with(binding) {
            searchView.editText.addTextChangedListener {
                if (it != null)
                    searchLocationViewModel.search(it.toString())
            }

            searchView.editText.setOnEditorActionListener { textView, i, keyEvent ->
                if (i == EditorInfo.IME_ACTION_SEARCH || (keyEvent.keyCode == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_DOWN)) {
                    searchLocationViewModel.search(textView.text.toString())
                    true
                } else {
                    false
                }
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

        searchLocationViewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.data != null) {
                showSearchLocationList(state.data)
            }

            if (state.message.isNotBlank()) {
                Toast.makeText(requireActivity(), state.message, Toast.LENGTH_SHORT).show()
            }
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