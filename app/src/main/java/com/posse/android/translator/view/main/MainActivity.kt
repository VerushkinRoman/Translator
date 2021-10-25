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
import androidx.recyclerview.widget.LinearLayoutManager
import com.posse.android.translator.R
import com.posse.android.translator.databinding.ActivityMainBinding
import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.presenter.Presenter
import com.posse.android.translator.utils.AndroidNetworkStatus
import com.posse.android.translator.utils.NetworkStatus
import com.posse.android.translator.view.base.View
import com.posse.android.translator.view.main.adapter.MainAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity(), View {

    private lateinit var binding: ActivityMainBinding

    private lateinit var presenter: Presenter<AppState, View>

    private lateinit var networkStatus: NetworkStatus

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
        binding = ActivityMainBinding.inflate(layoutInflater)
        presenter = MainPresenterImpl()
        networkStatus = AndroidNetworkStatus(this)
        networkStatus
            .isOnline()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
                presenter.getData(binding.searchField.editText?.text.toString(), isOnline)
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

    override fun onStart() {
        super.onStart()
        presenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView(this)
    }
}