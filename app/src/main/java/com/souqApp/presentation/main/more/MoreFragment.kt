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
import com.souqApp.presentation.login.LoginActivity


class MoreFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentMoreBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
    }

    private fun initListeners() {
        binding.imgFacebook.setOnClickListener(this)
        binding.imgTiktok.setOnClickListener(this)
        binding.imgInstagram.setOnClickListener(this)
        binding.includeLogin.root.setOnClickListener(this)
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
        }
    }

    private fun goToLoginActivity() {
        startActivity(Intent(activity, LoginActivity::class.java))
    }
}