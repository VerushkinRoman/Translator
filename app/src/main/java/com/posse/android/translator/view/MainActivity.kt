package com.posse.android.translator.view

import android.os.Bundle
import android.os.PersistableBundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.posse.android.translator.R
import com.posse.android.translator.databinding.ActivityMainBinding
import com.posse.android.translator.utils.NetworkStatus
import com.posse.android.translator.view.history.HistoryFragment
import com.posse.android.translator.view.main.MainFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private val networkStatus: NetworkStatus by inject()

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var isBackShown = false

    private var lastTimeBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupOnlineStatus()
        setContentView(binding.root)
        val startPage: Int = savedInstanceState?.getInt(KEY_SELECTED) ?: R.id.bottomMain
        initView(startPage)
        getSystemService<InputMethodManager>()
    }

    private fun initView(@IdRes startPage: Int) {

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottomMain -> replaceFragment(MainFragment.newInstance())
                R.id.bottomHistory -> replaceFragment(HistoryFragment.newInstance())
                else -> replaceFragment(MainFragment.newInstance())
            }
            true
        }

        binding.bottomNavigation.selectedItemId = startPage
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
            .replace(R.id.mainContainer, fragment)
            .commit()

        isBackShown = false
    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt(KEY_SELECTED, binding.bottomNavigation.selectedItemId)
    }

    override fun onBackPressed() {
        if (binding.bottomNavigation.selectedItemId == R.id.bottomMain) {
            checkExit()
        } else {
            binding.bottomNavigation.selectedItemId = R.id.bottomMain
        }
        lastTimeBackPressed = System.currentTimeMillis()
    }

    private fun checkExit() {
        Toast.makeText(this, getString(R.string.back_again_to_exit), Toast.LENGTH_SHORT).show()
        if (System.currentTimeMillis() - lastTimeBackPressed < BACK_BUTTON_EXIT_DELAY && isBackShown) {
            exitProcess(0)
        }
        isBackShown = true
    }


    private fun setupOnlineStatus() {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            networkStatus.getStatus().collect { onlineStatus ->
                if (onlineStatus) {
                    binding.offlineStatus.visibility = GONE
                } else {
                    binding.offlineStatus.visibility = VISIBLE
                }
            }
        }
    }

    companion object {
        private const val KEY_SELECTED = "Selected item"
        private const val BACK_BUTTON_EXIT_DELAY = 3000
    }
}