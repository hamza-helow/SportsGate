package com.souqApp.presentation.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.souqApp.databinding.ActivitySplashBinding
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@DelicateCoroutinesApi
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}
