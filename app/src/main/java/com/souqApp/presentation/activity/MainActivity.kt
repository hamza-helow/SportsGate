package com.souqApp.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.souqApp.R
import com.souqApp.databinding.ActivityMainBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.utils.AppBarConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AppBarConfig {

    private lateinit var binding: ActivityMainBinding

    private val appBarConfiguration: AppBarConfiguration by lazy { AppBarConfiguration(navController.graph) }

    private val navController: NavController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }

    private val bottomNavigationItems = listOf(
        R.id.homeFragment,
        R.id.categoriesFragment,
        R.id.cartFragment,
        R.id.moreFragment
    )

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupBottomNav()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isBottomNavigationItem = bottomNavigationItems.contains(destination.id)
            binding.bottomNavigationView.isVisible(isBottomNavigationItem)
            binding.toolbar.title = destination.label
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
}