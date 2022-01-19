package com.souqApp.presentation.main

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.marginEnd
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

        @BindingAdapter(value = ["networkImage", "placeholder"], requireAll = false)
        @JvmStatic
        fun ImageView.setImageUrl(url: String, placeholder: Drawable? = null) {
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
                layoutParams.rightMargin, Math.round(bottomMargin)
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