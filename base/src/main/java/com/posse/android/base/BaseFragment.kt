package com.posse.android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.posse.android.adapter.MainAdapter
import com.posse.android.base.databinding.MainScreenLayoutBinding
import com.posse.android.description.DescriptionFragment
import com.posse.android.models.AppState
import com.posse.android.models.DataModel
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseFragment : Fragment(), com.posse.android.base.View {

    protected val model: MainViewModel by viewModel()

    private val adapter: MainAdapter by lazy { MainAdapter(onListItemClickListener, listOf()) }

    private var _binding: MainScreenLayoutBinding? = null
    protected val binding get() = _binding!!

    protected val onListItemClickListener: MainAdapter.OnListItemClickListener =
        object : MainAdapter.OnListItemClickListener {
            override fun onItemClick(data: DataModel) {
                DescriptionFragment.newInstance(data).show(childFragmentManager, null)
            }
        }

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
        setupSearchField()
        binding.mainRecyclerview.adapter = adapter
    }

    protected abstract fun setupSearchField()

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
}