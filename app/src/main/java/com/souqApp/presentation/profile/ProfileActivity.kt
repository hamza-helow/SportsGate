package com.souqApp.presentation.profile

import android.Manifest
import android.app.Activity
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.souqApp.databinding.ActivityProfileBinding
import com.souqApp.infra.extension.setup
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.souqApp.infra.utils.PathUtil
import java.io.File


@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityProfileBinding

    val viewModel: ProfileViewModel by viewModels()

    private lateinit var cameraCachePath: File
    private val uniqueKey = System.currentTimeMillis().toString()

    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private lateinit var getImageContent: ActivityResultLauncher<String>
    private lateinit var takePicture: ActivityResultLauncher<Uri>

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeAppBar.toolbar);
        supportActionBar?.setup()

        binding.user = sharedPrefs.getUserInfo()

        cameraCachePath = File(cacheDir, "camera")

        init()

        requestCameraPermission =
            activityResultRegistry.register("requestCameraPermission_$uniqueKey",
                this, ActivityResultContracts.RequestPermission(),
                ActivityResultCallback<Boolean> { isGranted ->
                    if (isGranted) {
                        getImageContent.launch("image/*")
                    }
                })


        getImageContent = activityResultRegistry.register("getImageContent_$uniqueKey",
            this, ActivityResultContracts.GetContent(),
            { uri ->

                Log.e("ERer", uri.toString())

                val filePath: String =
                    PathUtil.getPath(baseContext, uri)

                Log.e("ERer", filePath)

                viewModel.updateProfile("hamza al-helow",filePath)

            })

    }

    private fun init() {

        binding.imgProfile.setOnClickListener(this)

        viewModel.updateProfile("hamza al-helow","/storage/emulated/0/DCIM/Camera/IMG_20211231_235711.jpg")

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun onClick(view: View) {

        when (view.id) {

            binding.imgProfile.id -> updateImage()

        }

    }

    private fun updateImage() {
        requestPermissionAndTakePicture()


    }


    private fun requestPermissionAndTakePicture() {
        requestCameraPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }


}



