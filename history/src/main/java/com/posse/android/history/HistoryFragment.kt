package com.posse.android.history

import android.os.Bundle
import android.view.View
import com.posse.android.base.BaseFragment
import com.posse.android.data.datasource.RoomDataBaseImplementation.Companion.HISTORY

class HistoryFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.getWordDescriptions(HISTORY, false)
    }

    override fun setupSearchField() {
        binding.searchField.visibility = View.GONE
    }

    companion object {
        fun newInstance(): HistoryFragment = HistoryFragment()
    }
}