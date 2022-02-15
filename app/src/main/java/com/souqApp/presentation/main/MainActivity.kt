package com.souqApp.presentation.main

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
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
import android.widget.TextView
import androidx.core.text.HtmlCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.souqApp.infra.extension.inVisible
import com.souqApp.infra.extension.setLocale
import com.souqApp.infra.utils.SharedPrefs
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = binding.bottomNavigationView

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

        setLocale(sharedPrefs.getLanguage())

        // set navigation graph
        navController.setGraph(R.navigation.home_nav_graph)
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onResume() {
        binding.bottomNavigationView.menu.getItem(2).isEnabled = sharedPrefs.isLogin()
        super.onResume()
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
                    } else {
                        placeholder(R.drawable.image_placeholder)
                    }

                }
                .noFade()
                .into(this)
        }

        @BindingAdapter("horizontalPadding")
        @JvmStatic
        fun setHorizontalPadding(view: View, margin: Float) {
            view.setPadding(0, 0, margin.toInt(), 0)
        }


        @BindingAdapter("android:text")
        @JvmStatic
        fun setHtmlText(view: TextView, text: String) {
            view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT)
            } else {
                HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY);
            }

            view.movementMethod = LinkMovementMethod.getInstance();
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

        @BindingAdapter("inVisible")
        @JvmStatic
        fun inVisible(view: View, inVisible: Boolean) {
            view.inVisible(inVisible)
        }

    }
}