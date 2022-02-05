package com.souqApp.presentation.addresses.add_address

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import com.souqApp.R
import com.souqApp.databinding.FragmentAddAddressBinding
import com.souqApp.presentation.addresses.map.MapsActivity


class AddAddressFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentAddAddressBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAddressBinding.inflate(inflater, container, false)

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                NavHostFragment.findNavController(this@AddAddressFragment).navigateUp();
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        binding.cardPickLocation.setOnClickListener(this)
    }

    companion object {

        @JvmStatic
        fun newInstance() = AddAddressFragment()
    }

    override fun onClick(view: View) {

        when (view.id) {

            binding.cardPickLocation.id -> goToMapActivity()
        }
    }

    private fun goToMapActivity() {
        startActivity(Intent(requireActivity(), MapsActivity::class.java))
    }
}