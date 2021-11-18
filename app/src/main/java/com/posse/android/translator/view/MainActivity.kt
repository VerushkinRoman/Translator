package com.posse.android.translator.view

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import by.kirich1409.viewbindingdelegate.viewBinding
import com.posse.android.history.HistoryFragment
import com.posse.android.main.MainFragment
import com.posse.android.network.NetworkStatus
import com.posse.android.translator.R
import com.posse.android.translator.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val networkStatus: NetworkStatus by inject()

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    private var isBackShown = false

    private var lastTimeBackPressed: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setDefaultSplashScreen()

        setupOnlineStatus()
        setContentView(binding.root)
        val startPage: Int = savedInstanceState?.getInt(KEY_SELECTED) ?: R.id.bottomMain
        initView(startPage)
        getSystemService<InputMethodManager>()
    }

    private fun setDefaultSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            setSplashScreenHideAnimation()
        }
        showSplashDelay()
    }

    @RequiresApi(31)
    private fun setSplashScreenHideAnimation() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            val slideLeft = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_X,
                0f,
                -splashScreenView.width.toFloat()
            )
            slideLeft.interpolator = AnticipateInterpolator()
            slideLeft.duration = SLIDE_LEFT_DURATION

            slideLeft.doOnEnd { splashScreenView.remove() }
            slideLeft.start()
        }
    }

    private fun showSplashDelay() {
        var isHideSplashScreen = false

        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                isHideSplashScreen = true
            }
        }.start()

        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (isHideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
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
        private const val SLIDE_LEFT_DURATION = 2000L
    }
}