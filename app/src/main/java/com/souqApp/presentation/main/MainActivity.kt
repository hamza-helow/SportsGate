package com.souqApp.presentation.main

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.souqApp.infra.utils.KeepStateNavigator


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    companion object {
        @BindingAdapter("networkImage")
        @JvmStatic
        fun setImageUrl(view: ImageView, path: String) {
            Picasso.get().load(path).into(view)
        }

        @BindingAdapter("horizontalMargin")
        @JvmStatic
        fun setBottomMargin(view: View, margin: Float) {
            view.setPadding(0, 0, margin.toInt(), 0)
        }
    }
}