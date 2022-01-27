package com.souqApp.presentation.main.home

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.view.ViewGroup
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.souqApp.R
import com.souqApp.data.main.home.remote.dto.ProductAdsResponse
import com.souqApp.databinding.ItemSliderBinding
import com.squareup.picasso.Picasso
import java.util.*

class SliderViewPagerAdapter(val context: Context, private val ads: List<ProductAdsResponse>) :
    PagerAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return ads.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ItemSliderBinding = ItemSliderBinding.inflate(inflater, container, false)
       // Picasso.get().load(ads[position].image).into(binding.image)
        binding.setImage(ads[position].image)
        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}