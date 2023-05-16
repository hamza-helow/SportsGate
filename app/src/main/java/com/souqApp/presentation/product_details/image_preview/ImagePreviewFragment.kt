package com.souqApp.presentation.product_details.image_preview

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.souqApp.databinding.FragmentImagePreviewBinding
import com.souqApp.infra.utils.setImageUrl
import com.souqApp.presentation.base.BaseFragment


class ImagePreviewFragment :
    BaseFragment<FragmentImagePreviewBinding>(FragmentImagePreviewBinding::inflate) {

    private val args: ImagePreviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.img.setImageUrl(args.image)
    }


}