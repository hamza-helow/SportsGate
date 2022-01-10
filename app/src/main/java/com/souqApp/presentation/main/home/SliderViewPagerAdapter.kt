package com.souqApp.presentation.main.home

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.widget.LinearLayout
import android.view.ViewGroup
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import com.souqApp.R
import com.souqApp.databinding.ItemSliderBinding
import java.util.*

class SliderViewPagerAdapter(val context: Context) : PagerAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return 5
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: ItemSliderBinding = ItemSliderBinding.inflate(inflater, container, false)

        val rnd = Random()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

        binding.image.setColorFilter(color)

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}