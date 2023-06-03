package com.souqApp.presentation.main.more

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.databinding.FragmentMoreBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.openUrl
import com.souqApp.infra.utils.APP_TAG
import com.souqApp.infra.utils.MIN_ANDROID_VERSION
import com.souqApp.infra.utils.ORDER_HISTORY_ANDROID
import com.souqApp.infra.utils.SHOW_COPYRIGHTS
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>(FragmentMoreBinding::inflate),
    View.OnClickListener {

    private val viewModel: MoreViewModel by viewModels()

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initInfo()
        observer()
    }

    private fun observer() {
        viewModel.state.observe(viewLifecycleOwner) { handleState(it) }
    }

    private fun handleState(state: MoreFragmentState) {
        when (state) {
            is MoreFragmentState.Error -> onError(state.throwable)
            is MoreFragmentState.ErrorLoad -> onErrorLoad(state.response)
            is MoreFragmentState.Loaded -> onLoaded(state.settingEntity)
            is MoreFragmentState.Loading -> Unit
        }
    }

    private fun onLoaded(settingEntity: SettingsEntity) {
        binding.imgFacebook.contentDescription = settingEntity.facebook
        binding.imgInstagram.contentDescription = settingEntity.instagram
        binding.imgTiktok.contentDescription = settingEntity.tiktok
    }

    private fun onErrorLoad(response: WrappedResponse<SettingsEntity>) {
        showDialog(response.message)
        binding.imgTiktok.isVisible(false)
        binding.imgInstagram.isVisible(false)
        binding.imgFacebook.isVisible(false)
    }

    private fun onError(throwable: Throwable) {
        Log.e(APP_TAG, throwable.stackTraceToString())
    }

    private fun initListener() {
        binding.txtChangeLanguage.setOnClickListener(this)
        binding.txtOrders.setOnClickListener(this)
        binding.txtAddresses.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)
        binding.imgTiktok.setOnClickListener(this)
        binding.imgInstagram.setOnClickListener(this)
        binding.txtLogin.setOnClickListener(this)
        binding.cardProfile.setOnClickListener(this)
        binding.txtShareApp.setOnClickListener(this)
        binding.txtContactUs.setOnClickListener(this)
        binding.txtWishList.setOnClickListener(this)
        binding.txtChangePassword.setOnClickListener(this)
        binding.txtAboutUs.setOnClickListener(this)
        binding.txtTermsAndConditions.setOnClickListener(this)
    }

    override fun onClick(p0: View) {

        when (p0.id) {
            binding.imgFacebook.id -> openLink(binding.imgFacebook)
            binding.imgTiktok.id -> openLink(binding.imgTiktok)
            binding.imgInstagram.id -> openLink(binding.imgInstagram)
            binding.txtLogin.id -> {
                navigate(MoreFragmentDirections.toAuthGraph())
            }
            binding.cardProfile.id -> {
                navigate(MoreFragmentDirections.toProfileFragment())
            }
            binding.txtChangePassword.id -> {
                navigate(MoreFragmentDirections.toChangePasswordFragment())

            }
            binding.txtAddresses.id -> {
                navigate(MoreFragmentDirections.toAddressesGraph())
            }
            binding.txtOrders.id -> {
                navigate(MoreFragmentDirections.toOrdersGraph())
            }
            binding.txtShareApp.id -> shareApp()
            binding.txtContactUs.id -> {
                navigate(MoreFragmentDirections.toContactUsFragment())

            }
            binding.txtWishList.id -> {
                navigate(MoreFragmentDirections.toWishListFragment())
            }
            binding.txtChangeLanguage.id -> {
                navigate(MoreFragmentDirections.toChangeLanguageFragment())
            }
            binding.txtAboutUs.id -> {
                navigate(MoreFragmentDirections.toAboutUsFragment())

            }
            binding.txtTermsAndConditions.id -> {
                navigate(MoreFragmentDirections.toTermsAndConditionsFragment())
            }
        }
    }

    private fun openLink(imageView: ImageView) {
        requireContext().openUrl(imageView.contentDescription.toString())
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Share App"
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here")
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
        startActivity(Intent.createChooser(sharingIntent, "Share via"))
    }

    override fun onResume() {
        super.onResume()
        initInfo()
    }

    private fun initInfo() {
        binding.sharedPrefs = sharedPrefs
        binding.user = sharedPrefs.getUserInfo()
        binding.enableCopyrights = firebaseConfig.getBoolean(SHOW_COPYRIGHTS)
        binding.enableOrderHistory = firebaseConfig.getBoolean(ORDER_HISTORY_ANDROID)
        binding.appVersion = firebaseConfig.getString(MIN_ANDROID_VERSION)
    }
}

