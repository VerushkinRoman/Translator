package com.posse.android.main

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.core.widget.doOnTextChanged
import com.posse.android.base.BaseFragment
import com.posse.android.network.NetworkStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class MainFragment : BaseFragment(), KoinComponent {

    private val networkStatus: NetworkStatus by inject()

    private var isOnline = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnlineStatus()
    }

    override fun setupSearchField() {
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
                val text = binding.searchField.editText?.text?.toString().orEmpty()
                if (checkValidText(text)) {
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

    private fun setupOnlineStatus() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            networkStatus.getStatus().collect { onlineStatus ->
                isOnline = onlineStatus
            }
        }
    }

    companion object {
        fun newInstance(): MainFragment = MainFragment()

        fun checkValidText(text: String) = !text.contains(" ") && text.isNotEmpty()
    }
}