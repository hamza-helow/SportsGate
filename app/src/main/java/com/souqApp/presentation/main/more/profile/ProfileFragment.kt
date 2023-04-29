package com.souqApp.presentation.main.more.profile

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.souqApp.R
import com.souqApp.databinding.FragmentProfileBinding
import com.souqApp.domain.common.entity.UserEntity
import com.souqApp.infra.extension.showToast
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.PathUtil
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::inflate),
    View.OnClickListener {

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    private val requestReadStoragePermission: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionGranted ->
            if (permissionGranted) {
                getImageContent.launch("image/*")
            }
        }

    private val getImageContent: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageSelected = PathUtil.getPath(requireContext(), uri)
                binding.imgProfile.setImageURI(uri)
                viewModel.setProfileChanged(true)
            }
        }


    private var imageSelected: String? = null
    val viewModel: ProfileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onResume() {
        super.onResume()
        initListener()
    }

    override fun onStart() {
        super.onStart()
        observe()
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
        binding.user = sharedPrefs.getUserInfo()
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
        sharedPrefs.saveUserInfo(userEntity)
        requireContext().showToast(getString(R.string.updated_successfully))
    }

    private fun handleProfileChanged(changed: Boolean) {
        binding.btnSave.isEnabled = changed
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.imgProfile.id -> requestPermissionReadStorage()
            binding.btnSave.id -> updateProfile()
            binding.btnLogout.id -> logout()
        }
    }

    private fun logout() {
        showDialog(
            message = getString(R.string.your_account_will_be_logged_out_from_the_app),
            confirmText = getString(R.string.log_out),
            cancelTest = getString(R.string.cancel),
            onConfirm = {
                sharedPrefs.logout()
                findNavController().popBackStack()
            }
        )
    }

    private fun updateProfile() {
        val name = binding.etName.text.toString().trim()
        viewModel.updateProfile(name, imageSelected.orEmpty())
    }

    private fun requestPermissionReadStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestReadStoragePermission.launch(Manifest.permission.READ_MEDIA_IMAGES)
        } else {
            requestReadStoragePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requestReadStoragePermission.unregister()
        getImageContent.unregister()
    }
}