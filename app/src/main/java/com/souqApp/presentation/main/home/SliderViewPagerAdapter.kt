package com.souqApp.presentation.main.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.souqApp.data.main.home.remote.dto.ProductAdsEntity
import com.souqApp.databinding.ItemSliderBinding

class SliderViewPagerAdapter(
    val context: Context,
    private val ads: List<ProductAdsEntity>,
    private val viewOnly: Boolean = false ,
    val onClickItem:(Int)->Unit ={}
) :
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
        binding.setImage(ads[position].image)
        container.addView(binding.root)

        if (!viewOnly)
            binding.root.setOnClickListener {
                onClickItem(ads[position].id)
            }

        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}