package com.souqApp.presentation.main

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.souqApp.R
import com.souqApp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import com.squareup.picasso.Picasso
import androidx.navigation.plusAssign
import androidx.navigation.ui.setupWithNavController
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.utils.KeepStateNavigator
import android.view.ViewGroup.MarginLayoutParams
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // get fragment
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)!!

        val navigator = KeepStateNavigator(
            this,
            navHostFragment.childFragmentManager,
            R.id.nav_host_fragment_content_main
        )
        navController.navigatorProvider += navigator

        // set navigation graph
        navController.setGraph(R.navigation.home_nav_graph)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    private fun observer() {
        viewModel.mState
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { handleState(it) }
            .launchIn(lifecycleScope)
    }


    override fun onStart() {
        super.onStart()
        observer()
    }


    private fun handleState(state: MainActivityState) {
        when (state) {

            is MainActivityState.Init -> Unit
            is MainActivityState.OnNavigationChanged -> handleOnNavigationChanged(state.idNav)

        }
    }

    private fun handleOnNavigationChanged(idNav: Int) {
        binding.bottomNavigationView.selectedItemId = idNav
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    companion object {

        @BindingAdapter(value = ["networkImage", "placeholder"], requireAll = false)
        @JvmStatic
        fun ImageView.setImageUrl(url: String?, placeholder: Drawable? = null) {

            if (url == null || url.isEmpty())
                return

            Picasso
                .get()
                .load(url).apply {
                    if (placeholder != null) {
                        placeholder(placeholder)
                    }
                }.into(this)
        }

        @BindingAdapter("horizontalPadding")
        @JvmStatic
        fun setHorizontalPadding(view: View, margin: Float) {
            view.setPadding(0, 0, margin.toInt(), 0)
        }


        @BindingAdapter("layout_marginBottom")
        @JvmStatic
        fun setBottomMargin(view: View, bottomMargin: Float) {
            val layoutParams = view.layoutParams as MarginLayoutParams
            layoutParams.setMargins(
                layoutParams.leftMargin, layoutParams.topMargin,
                layoutParams.rightMargin, bottomMargin.roundToInt()
            )
            view.layoutParams = layoutParams
        }

        @BindingAdapter("isVisible")
        @JvmStatic
        fun isVisible(view: View, isVisible: Boolean) {
            view.isVisible(isVisible)
        }
    }
}