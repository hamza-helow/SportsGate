package com.souqApp.presentation.main.more

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.souqApp.R
import com.souqApp.databinding.FragmentMoreBinding
import com.souqApp.infra.extension.openUrl
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.addresses.AddressActivity
import com.souqApp.presentation.login.LoginActivity
import com.souqApp.presentation.main.more.changePassword.ChangePasswordActivity
import com.souqApp.presentation.main.more.profile.ProfileActivity
import com.souqApp.presentation.orders.OrdersActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoreFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentMoreBinding

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
        init()
    }

    override fun onResume() {
        super.onResume()
        binding.sharedPrefs = sharedPrefs
        binding.user = sharedPrefs.getUserInfo()
    }

    private fun init() {
        binding.includeCardChangePassword.root.setOnClickListener(this)
        binding.cardOrders.setOnClickListener(this)
        binding.cardAddresses.setOnClickListener(this)
        binding.imgFacebook.setOnClickListener(this)
        binding.imgTiktok.setOnClickListener(this)
        binding.imgInstagram.setOnClickListener(this)
        binding.includeLogin.root.setOnClickListener(this)
        binding.cardProfile.setOnClickListener(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = MoreFragment()
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            binding.imgFacebook.id -> requireContext().openUrl(getString(R.string.facebook_link))
            binding.imgTiktok.id -> requireContext().openUrl(getString(R.string.tiktok_link))
            binding.imgInstagram.id -> requireContext().openUrl(getString(R.string.instagram_link))
            binding.includeLogin.root.id -> goToLoginActivity()
            binding.cardProfile.id -> goToProfileActivity()
            binding.includeCardChangePassword.root.id -> goToChangePasswordActivity()
            binding.cardAddresses.id -> goToAddressActivity()
            binding.cardOrders.id -> goToOrdersActivity()
        }
    }

    private fun goToOrdersActivity() {
        startActivity(Intent(requireActivity(), OrdersActivity::class.java))
    }

    private fun goToAddressActivity() {
        startActivity(Intent(requireActivity(), AddressActivity::class.java))
    }

    private fun goToChangePasswordActivity() {
        startActivity(Intent(requireActivity(), ChangePasswordActivity::class.java))
    }

    private fun goToProfileActivity() {
        startActivity(Intent(requireActivity(), ProfileActivity::class.java))
    }

    private fun goToLoginActivity() {
        startActivity(Intent(activity, LoginActivity::class.java))
    }
}