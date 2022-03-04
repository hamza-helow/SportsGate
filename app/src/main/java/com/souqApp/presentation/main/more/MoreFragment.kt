package com.souqApp.presentation.main.more

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.settings.remote.dto.SettingsEntity
import com.souqApp.databinding.FragmentMoreBinding
import com.souqApp.infra.extension.changeStatusBarColor
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.openUrl
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.utils.*
import com.souqApp.presentation.addresses.AddressActivity
import com.souqApp.presentation.common.ChangeLanguageDialog
import com.souqApp.presentation.login.LoginActivity
import com.souqApp.presentation.main.more.about_us.AboutUsActivity
import com.souqApp.presentation.main.more.changePassword.ChangePasswordActivity
import com.souqApp.presentation.main.more.contact_us.ContactUsActivity
import com.souqApp.presentation.main.more.profile.ProfileActivity
import com.souqApp.presentation.main.more.terms_and_conditions.TermsAndConditionsActivity
import com.souqApp.presentation.main.more.wish_list.WishListActivity
import com.souqApp.presentation.orders.OrdersActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentMoreBinding
    private val viewModel: MoreViewModel by viewModels()

    @Inject
    lateinit var firebaseConfig: FirebaseRemoteConfig

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initInfo()
        observer()
    }

    private fun observer() {
        viewModel.state.observe(this, { handleState(it) })
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
        requireContext().showGenericAlertDialog(response.formattedErrors())

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

    companion object {
        @JvmStatic
        fun newInstance() = MoreFragment()
    }

    override fun onClick(p0: View) {

        when (p0.id) {
            binding.imgFacebook.id -> openLink(binding.imgFacebook)
            binding.imgTiktok.id -> openLink(binding.imgTiktok)
            binding.imgInstagram.id -> openLink(binding.imgInstagram)
            binding.includeLogin.root.id -> goTo(LoginActivity::class.java)
            binding.cardProfile.id -> goTo(ProfileActivity::class.java)
            binding.cardChangePassword.id -> goTo(ChangePasswordActivity::class.java)
            binding.cardAddresses.id -> goTo(AddressActivity::class.java)
            binding.cardOrders.id -> goTo(OrdersActivity::class.java)
            binding.cardShareApp.id -> shareApp()
            binding.cardContactUs.id -> goToContactUsActivity()
            binding.cardWishList.id -> goTo(WishListActivity::class.java)
            binding.cardChangeLanguage.id -> openChangeLanguageDialog()
            binding.cardAboutUs.id -> goTo(AboutUsActivity::class.java)
            binding.cardTermsAndConditions.id -> goTo(TermsAndConditionsActivity::class.java)
        }
    }

    private fun <T> goTo(to: Class<T>) {
        startActivity(Intent(requireActivity(), to))
    }

    private fun openLink(imageView: ImageView) {
        requireContext().openUrl(imageView.contentDescription.toString())
    }

    private fun openChangeLanguageDialog() {
        ChangeLanguageDialog(requireActivity(), language = sharedPrefs.getLanguage()).apply {
            show()
            onSave = {
                requireActivity().recreate()
                sharedPrefs.setLanguage(it)
            }
        }

    }

    private fun goToContactUsActivity() {
        startActivity(Intent(requireActivity(), ContactUsActivity::class.java))
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
        requireActivity().changeStatusBarColor()
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

