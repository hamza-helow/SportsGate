package com.souqApp.presentation.main.more.profile

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.souqApp.databinding.ActivityProfileBinding
import com.souqApp.infra.extension.setup
import com.souqApp.infra.utils.SharedPrefs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.souqApp.data.common.mapper.toUserResponse
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.PathUtil
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity(), View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs
    private lateinit var binding: ActivityProfileBinding
    private lateinit var cameraCachePath: File
    private lateinit var requestCameraPermission: ActivityResultLauncher<String>
    private lateinit var getImageContent: ActivityResultLauncher<String>
    private var imageSelected: String? = null
    val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initListener()
        initResultLaunchers()
    }

    override fun onStart() {
        super.onStart()
        observe()
    }

    private fun initResultLaunchers() {
        requestCameraPermission = activityResultRegistry
            .register("requestCameraPermission",
                this, ActivityResultContracts.RequestPermission(), { isGranted ->
                    if (isGranted) {
                        getImageContent.launch("image/*")
                    }
                })

        getImageContent = activityResultRegistry.register("getImageContent",
            this, ActivityResultContracts.GetContent(),
            { uri ->
                //check image is selected
                if (uri != null) {
                    imageSelected = PathUtil.getPath(baseContext, uri)
                    binding.imgProfile.setImageURI(uri)
                    viewModel.setProfileChanged(true)
                }
            })
    }

    private fun initListener() {
        binding.imgProfile.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.etName.doAfterTextChanged {
            val name = it.toString()
            if (name != sharedPrefs.getUserInfo()?.name) {
                viewModel.setProfileChanged(true)
            } else {
                viewModel.setProfileChanged(false)
            }
        }
    }

    private fun init() {
        //setup toolbar
        setSupportActionBar(binding.includeAppBar.toolbar)
        supportActionBar?.setup()
        binding.user = sharedPrefs.getUserInfo()
        cameraCachePath = File(cacheDir, "images")
    }

    private fun observe() {
        viewModel.mState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: ProfileActivityState) {
        when (state) {
            is ProfileActivityState.Init -> Unit
            is ProfileActivityState.ProfileChanged -> handleProfileChanged(state.isChanged)
            is ProfileActivityState.ErrorUpdateProfile -> Unit
            is ProfileActivityState.SuccessUpdateProfile -> handleSuccessUpdateProfile(state.userEntity)
            is ProfileActivityState.IsLoading -> handleIsLoading(state.isLoading)
            is ProfileActivityState.ShowToast -> Unit
        }
    }

    private fun handleIsLoading(isLoading: Boolean) {
        binding.includeLoader.loadingProgressBar.start(isLoading)
    }

    private fun handleSuccessUpdateProfile(userEntity: UserEntity) {
        sharedPrefs.saveUserInfo(userEntity.toUserResponse())
        showToast("Updated successfully")
    }

    private fun handleProfileChanged(changed: Boolean) {
        binding.btnSave.isEnabled = changed
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.imgProfile.id -> requestPermissionReadStorageAndSelectImage()
            binding.btnSave.id -> updateProfile()
            binding.btnLogout.id -> logout()
        }
    }

    private fun logout() {
        sharedPrefs.clear()
        finish()
    }

    private fun updateProfile() {
        val name = binding.etName.text.toString().trim()
        viewModel.updateProfile(name, imageSelected ?: "")
    }

    private fun requestPermissionReadStorageAndSelectImage() {
        requestCameraPermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onDestroy() {
        super.onDestroy()
        requestCameraPermission.unregister()
        getImageContent.unregister()
    }
}