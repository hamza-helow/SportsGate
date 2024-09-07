package com.souqApp.presentation.main.more.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.common.utlis.CacheHelper
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.databinding.FragmentMoreBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.openUrl
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

    private lateinit var pagesAdapter: PagesAdapter

    override fun showAppBar() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPagesAdapter()
        initListener()
        initInfo()
        observeToSettings()
    }

    private fun observeToSettings() {
        viewModel.settingsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> onErrorLoad(result.error)
                is BaseResult.Success -> onLoaded(result.data)
            }
        }
    }

    private fun initPagesAdapter() {
        val pages = CacheHelper.pages

        binding.txtPages.isVisible(pages.isNotEmpty())
        pagesAdapter = PagesAdapter { navigate(MoreFragmentDirections.toPageDetailsFragment(it)) }
        pagesAdapter.list = pages
        binding.recPages.layoutManager = LinearLayoutManager(requireContext())
        binding.recPages.adapter = pagesAdapter
    }

    private fun onLoaded(settingEntity: SettingsEntity) {
        viewModel.facebook = settingEntity.facebook.orEmpty()
        viewModel.instagram = settingEntity.instagram.orEmpty()
        viewModel.twitter = settingEntity.twitter.orEmpty()
        viewModel.tiktok = settingEntity.tiktok.orEmpty()
    }

    private fun onErrorLoad(response: WrappedResponse<SettingsEntity>) {
        showDialog(response.message)
        binding.imgTiktok.isVisible(false)
        binding.imgInstagram.isVisible(false)
        binding.imgFacebook.isVisible(false)
    }

    private fun initListener() {
        binding.txtChangeLanguage.setOnClickListener(this)
        binding.txtOrders.setOnClickListener(this)
        binding.txtAddresses.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)
        binding.imgTiktok.setOnClickListener(this)
        binding.imgTwitter.setOnClickListener(this)
        binding.imgInstagram.setOnClickListener(this)
        binding.txtLogin.setOnClickListener(this)
        binding.cardProfile.setOnClickListener(this)
        binding.txtShareApp.setOnClickListener(this)
        binding.txtContactUs.setOnClickListener(this)
        binding.txtWishList.setOnClickListener(this)
        binding.txtChangePassword.setOnClickListener(this)
    }

    override fun onClick(p0: View) {

        when (p0.id) {
            binding.imgFacebook.id -> openLink(viewModel.facebook)
            binding.imgTiktok.id -> openLink(viewModel.tiktok)
            binding.imgTwitter.id -> openLink(viewModel.twitter)
            binding.imgInstagram.id -> openLink(viewModel.instagram)
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

        }
    }

    private fun openLink(url: String) {
        requireContext().openUrl(url)
    }

    private fun shareApp() {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        val shareBody = "Share App"
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

