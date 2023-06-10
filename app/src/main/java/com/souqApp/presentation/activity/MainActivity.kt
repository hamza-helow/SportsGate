package com.souqApp.presentation.activity

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.navigation.FloatingWindow
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.souqApp.R
import com.souqApp.data.common.module.SharedPrefsEntryPoint
import com.souqApp.databinding.ActivityMainBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.setAppLocale
import com.souqApp.infra.utils.AppBarConfig
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.EntryPointAccessors
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppBarConfig {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private val appBarConfiguration: AppBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    private val navController: NavController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }

    private val bottomNavigationItems = listOf(
        R.id.homeFragment,
        R.id.categoriesFragment,
        R.id.cartFragment,
        R.id.moreFragment
    )

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        window.decorView.layoutDirection = resources.configuration.layoutDirection
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        navController.setGraph(R.navigation.nav_graph)

        viewModel.cartQtyLiveData.observe(this) {
            val badge = binding.bottomNavigationView.getOrCreateBadge(R.id.cart_graph)
            badge.badgeNumberLocale = Locale.ENGLISH
            badge.isVisible = it > 0
            badge.number = it
        }

        setupBottomNav()
        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination is FloatingWindow)
                return@addOnDestinationChangedListener

            binding.bottomNavigationView.isVisible(bottomNavigationItems.contains(destination.id))

            if (destination.label != null)
                binding.toolbar.title = destination.label

            Log.e("TAG" , destination.label.toString())
        }
    }

    private fun setupBottomNav() {
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun updateTitleBar(title: String) {
        binding.toolbar.title = title
    }

    override fun hideAppBar() {
        binding.toolbar.isVisible = false
    }

    override fun showAppBar() {
        binding.toolbar.isVisible = true
    }

    override fun hideBackButton(hide: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(hide.not())
        supportActionBar?.setHomeButtonEnabled(hide.not())
    }

    override fun attachBaseContext(newBase: Context) {
        val sharedPrefs = EntryPointAccessors.fromApplication(
            newBase,
            SharedPrefsEntryPoint::class.java
        ).sharedPrefs

        super.attachBaseContext(ContextWrapper(newBase.setAppLocale(sharedPrefs.getLanguage())))
    }

}