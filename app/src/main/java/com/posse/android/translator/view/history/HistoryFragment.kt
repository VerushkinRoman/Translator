package com.posse.android.translator.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.posse.android.translator.R
import com.posse.android.translator.databinding.MainScreenLayoutBinding
import com.posse.android.translator.model.data.AppState
import com.posse.android.translator.model.data.DataModel
import com.posse.android.translator.model.datasource.RoomDataBaseImplementation.Companion.HISTORY
import com.posse.android.translator.view.dialog.DescriptionFragment
import com.posse.android.translator.view.main.MainViewModel
import com.posse.android.translator.view.main.adapter.MainAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryFragment : Fragment(), com.posse.android.translator.view.base.View {

    private val model: MainViewModel by viewModel()

    private var _binding: MainScreenLayoutBinding? = null
    private val binding get() = _binding!!

    private val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                DescriptionFragment.newInstance(data).show(childFragmentManager,null)
            }
        }

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener, listOf()) }

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
        binding.searchField.visibility = View.GONE
        binding.mainRecyclerview.adapter = adapter
        model.getWordDescriptions(HISTORY, false)
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

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): HistoryFragment = HistoryFragment()
    }
}