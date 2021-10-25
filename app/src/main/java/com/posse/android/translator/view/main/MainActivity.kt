package com.posse.android.translator.view.main

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.posse.android.translator.R
import com.posse.android.translator.databinding.ActivityMainBinding
import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.rx.ISchedulerProvider
import com.posse.android.translator.utils.NetworkStatus
import com.posse.android.translator.view.base.View
import com.posse.android.translator.view.main.adapter.MainAdapter
import dagger.android.AndroidInjection
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : AppCompatActivity(), View {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var schedulerProvider: ISchedulerProvider

    @Inject
    lateinit var networkStatus: NetworkStatus

    private lateinit var binding: ActivityMainBinding

    private val model: MainViewModel by lazy {
        viewModelFactory.create(MainViewModel::class.java)
    }

    private var isOnline = true

    private var adapter: MainAdapter? = null
    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                Toast.makeText(this@MainActivity, data.text, Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidInjection.inject(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        model.getStateLiveData().observe(this) { renderData(it) }
        networkStatus
            .isOnline()
            .subscribeOn(schedulerProvider.io)
            .observeOn(schedulerProvider.ui)
            .subscribe { isOnline ->
                this.isOnline = isOnline
                if (isOnline) {
                    binding.offlineStatus.visibility = GONE
                } else {
                    binding.offlineStatus.visibility = VISIBLE
                }
            }
        setContentView(binding.root)
        setupSearchField()
    }

    private fun setupSearchField() {
        val executor = Executors.newSingleThreadScheduledExecutor()
        val search = Runnable {
            runOnUiThread {
                getSystemService<InputMethodManager>()
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
            if (actionId == EditorInfo.IME_ACTION_DONE) {
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
                    if (adapter == null) {
                        adapter = MainAdapter(onListItemClickListener, dataModel)
                        binding.mainRecyclerview.layoutManager =
                            LinearLayoutManager(applicationContext)
                        binding.mainRecyclerview.adapter =
                            adapter
                    } else {
                        adapter!!.setData(dataModel)
                    }
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
                binding.mainRecyclerview.visibility = GONE
                binding.loadingLayout.visibility = GONE
                binding.errorLayout.visibility = VISIBLE
            }
            is AppState.Loading -> {
                binding.mainRecyclerview.visibility = GONE
                binding.loadingLayout.visibility = VISIBLE
                binding.errorLayout.visibility = GONE
            }
            is AppState.Success -> {
                binding.mainRecyclerview.visibility = VISIBLE
                binding.loadingLayout.visibility = GONE
                binding.errorLayout.visibility = GONE
            }
        }
    }
}