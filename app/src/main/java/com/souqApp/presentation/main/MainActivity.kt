package com.souqApp.presentation.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.souqApp.R
import com.souqApp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.infra.utils.IS_PURCHASE_ENABLED
import com.souqApp.infra.utils.SharedPrefs
import javax.inject.Inject
import com.souqApp.infra.extension.*
import com.souqApp.presentation.login.LoginActivity

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MenuItem.OnMenuItemClickListener {

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNav: BottomNavigationView

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setLocale(sharedPrefs.getLanguage())
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = binding.bottomNavigationView

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // set navigation graph
        navController.setGraph(R.navigation.main_nav_graph)
        binding.bottomNavigationView.setupWithNavController(navController)

        changeStatusBarColor(color = R.color.tool_bar_color)

    }


    override fun onResume() {
        checkIsPurchaseEnabled()
        firebaseConfig.fetchAndActivate().addOnCompleteListener {
            if (it.isSuccessful) {
                checkIsPurchaseEnabled()
            }
        }
        super.onResume()
    }

    private fun checkIsPurchaseEnabled() {
        val isPurchaseEnabled = firebaseConfig.getBoolean(IS_PURCHASE_ENABLED)
        binding.bottomNavigationView.menu.getItem(2).isEnabled =
            isPurchaseEnabled

        binding.bottomNavigationView.menu.getItem(2).setOnMenuItemClickListener(this)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item == binding.bottomNavigationView.menu.getItem(2)) {
            if (!sharedPrefs.isLogin()) {
                startActivity(Intent(this, LoginActivity::class.java))
                return true
            }
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}