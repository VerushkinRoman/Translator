package com.posse.android.translator.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.posse.android.models.DataModel
import com.posse.android.translator.R
import com.posse.android.translator.databinding.MainScreenLayoutBinding
import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.utils.NetworkStatus
import com.posse.android.translator.view.dialog.DescriptionFragment
import com.posse.android.translator.view.main.adapter.MainAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MainFragment : Fragment(), KoinComponent, com.posse.android.translator.view.base.View {

    private val model: MainViewModel by viewModel()
    private val networkStatus: NetworkStatus by inject()

    private var _binding: MainScreenLayoutBinding? = null
    private val binding get() = _binding!!

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                DescriptionFragment.newInstance(data).show(childFragmentManager,null)
            }
        }

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener, listOf()) }

    private var isOnline = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainScreenLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getStateLiveData().observe(viewLifecycleOwner) { renderData(it) }
        setupOnlineStatus()
        setupSearchField()
        binding.mainRecyclerview.adapter = adapter
    }

    private fun setupSearchField() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        val search = Runnable {
            CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
                activity?.getSystemService<InputMethodManager>()
                    ?.hideSoftInputFromWindow(binding.searchField.windowToken, 0)
                binding.searchField.clearFocus()
                model.getWordDescriptions(binding.searchField.editText?.text.toString(), isOnline)
            }
        }
        var scheduler: ScheduledFuture<*>? = null

        binding.searchField.editText?.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()) {
                scheduler?.cancel(true)
                if (text?.contains(" ") == true) {
                    binding.searchField.error = getString(R.string.error_input)
                } else {
                    scheduler = executor.schedule(search, 2, TimeUnit.SECONDS)
                    binding.searchField.error = null
                }
                binding.searchField.isEndIconVisible = true
            } else {
                scheduler?.cancel(true)
                binding.searchField.isEndIconVisible = false
                binding.searchField.error = null
            }
        }

        binding.searchField.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val text = binding.searchField.editText?.text?.toString() ?: ""
                if (!text.contains(" ") && text.isNotEmpty()) {
                    scheduler?.cancel(true)
                    search.run()
                    return@setOnEditorActionListener true
                }
            }
            binding.searchField.clearFocus()
            false
        }

        binding.searchField.setEndIconOnClickListener {
            scheduler?.cancel(true)
            binding.searchField.editText?.setText("")
            binding.searchField.isEndIconVisible = false
        }
    }

    override fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val dataModel = appState.data
                if (dataModel == null || dataModel.isEmpty()) {
                    showErrorScreen(getString(R.string.empty_response))
                } else {
                    changeView(appState)
                    adapter.setData(dataModel)
                }
            }
            is AppState.Loading -> {
                changeView(appState)
            }
            is AppState.Error -> {
                showErrorScreen(appState.error?.message)
            }
        }
    }

    private fun showErrorScreen(error: String?) {
        changeView(AppState.Error(null))
        binding.errorText.text = error ?: getString(R.string.error)
    }

    private fun changeView(state: AppState) {
        when (state) {
            is AppState.Error -> {
                binding.mainRecyclerview.visibility = View.GONE
                binding.loadingLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.VISIBLE
            }
            is AppState.Loading -> {
                binding.mainRecyclerview.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
                binding.errorLayout.visibility = View.GONE
            }
            is AppState.Success -> {
                binding.mainRecyclerview.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.errorLayout.visibility = View.GONE
            }
        }
    }

    private fun setupOnlineStatus() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            networkStatus.getStatus().collect { onlineStatus ->
                isOnline = onlineStatus
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()
    }
}