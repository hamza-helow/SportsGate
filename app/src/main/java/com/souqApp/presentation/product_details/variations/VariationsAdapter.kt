package com.souqApp.presentation.product_details.variations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.souqApp.data.product_details.remote.CombinationOption
import com.souqApp.data.product_details.remote.Variation
import com.souqApp.databinding.ItemVariationBinding

class VariationsAdapter(
    private val variations: List<Variation>,
    private val combinationOptions: List<CombinationOption>,
    val onChange: (labels: String) -> Unit
) :
    RecyclerView.Adapter<VariationsAdapter.VariationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VariationViewHolder {
        return VariationViewHolder(
            ItemVariationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VariationViewHolder, position: Int) {
        val item = variations[position]
        holder.binding.txtLabel.text = item.label

        holder.binding.recVariationOptions.adapter =
            VariationOptionsAdapter(item, combinationOptions) {
                onChange(variations.joinToString(separator = " | ") { it.selectedValue })
            }
        holder.binding.recVariationOptions.layoutManager =
            LinearLayoutManager(holder.itemView.context, HORIZONTAL, false)


    }

    override fun getItemCount(): Int {
        return variations.size
    }


    class VariationViewHolder(val binding: ItemVariationBinding) : ViewHolder(binding.root)
}