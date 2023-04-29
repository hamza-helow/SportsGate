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
import com.souqApp.infra.utils.*
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
        binding.cardChangeLanguage.setOnClickListener(this)
        binding.cardOrders.setOnClickListener(this)
        binding.cardAddresses.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)
        binding.imgTiktok.setOnClickListener(this)
        binding.imgInstagram.setOnClickListener(this)
        binding.includeLogin.root.setOnClickListener(this)
        binding.cardProfile.setOnClickListener(this)
        binding.cardShareApp.setOnClickListener(this)
        binding.cardContactUs.setOnClickListener(this)
        binding.cardWishList.setOnClickListener(this)
        binding.cardChangePassword.setOnClickListener(this)
        binding.cardAboutUs.setOnClickListener(this)
        binding.cardTermsAndConditions.setOnClickListener(this)
    }

    override fun onClick(p0: View) {

        when (p0.id) {
            binding.imgFacebook.id -> openLink(binding.imgFacebook)
            binding.imgTiktok.id -> openLink(binding.imgTiktok)
            binding.imgInstagram.id -> openLink(binding.imgInstagram)
            binding.includeLogin.root.id -> {
                navigate(MoreFragmentDirections.toAuthGraph())
            }
            binding.cardProfile.id -> {
                navigate(MoreFragmentDirections.toProfileFragment())
            }
            binding.cardChangePassword.id -> {
                navigate(MoreFragmentDirections.toChangePasswordFragment())

            }
            binding.cardAddresses.id -> {
                navigate(MoreFragmentDirections.toAddressesGraph())
            }
            binding.cardOrders.id -> {
                navigate(MoreFragmentDirections.toOrdersGraph())
            }
            binding.cardShareApp.id -> shareApp()
            binding.cardContactUs.id -> {
                navigate(MoreFragmentDirections.toContactUsFragment())

            }
            binding.cardWishList.id -> {
                navigate(MoreFragmentDirections.toWishListFragment())
            }
            binding.cardChangeLanguage.id -> {
                navigate(MoreFragmentDirections.toChangeLanguageFragment())
            }
            binding.cardAboutUs.id -> {
                navigate(MoreFragmentDirections.toAboutUsFragment())

            }
            binding.cardTermsAndConditions.id -> {
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

