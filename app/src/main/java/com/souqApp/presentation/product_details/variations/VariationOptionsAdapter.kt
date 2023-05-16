package com.souqApp.presentation.product_details.variations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.souqApp.BR
import com.souqApp.data.product_details.remote.CombinationOption
import com.souqApp.data.product_details.remote.Variation
import com.souqApp.data.product_details.remote.VariationType
import com.souqApp.databinding.ItemVariationColorsBinding
import com.souqApp.databinding.ItemVariationImageBinding
import com.souqApp.databinding.ItemVariationTextBinding
import com.souqApp.infra.extension.setHexColor
import com.souqApp.infra.utils.BaseViewHolder

class VariationOptionsAdapter(
    private val variation: Variation,
    combinationOptions: List<CombinationOption>,
    val onChange: () -> Unit
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var selectedItemPosition = 0

    init {
        val combinationOption = combinationOptions.firstOrNull { it.variationId == variation.id }
        val selectedOption = variation.options.firstOrNull { it.value == combinationOption?.value }
        selectedItemPosition = variation.options.indexOf(selectedOption)

        variation.selectedValue = variation.options[selectedItemPosition].value
    }

    override fun getItemViewType(position: Int): Int {
        return variation.type.code
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val layoutInflater = LayoutInflater.from(parent.context)
        when (viewType) {
            VariationType.COLOR.code -> return ViewHolderColorOption(
                ItemVariationColorsBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )

            VariationType.IMAGE.code -> {
                return ViewHolderImageOption(
                    ItemVariationImageBinding.inflate(
                        layoutInflater,
                        parent,
                        false
                    )
                )
            }

            else -> return ViewHolderTextOption(
                ItemVariationTextBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        val item = variation.options[position]

        holder.binding.root.setOnClickListener {

            if (selectedItemPosition != position) {
                selectedItemPosition = holder.layoutPosition
                notifyDataSetChanged()
                variation.selectedValue = item.value
                onChange()
            }
        }

        holder.bind(BR.selected, selectedItemPosition == position)

        when (holder) {
            is ViewHolderColorOption -> {
                holder.binding.txtColor.setHexColor(item.description)

            }

            is ViewHolderTextOption -> {
                holder.binding.txtValue.text = item.value
            }

            is ViewHolderImageOption -> {
                holder.binding.imageUrl = item.media.firstOrNull()
            }
        }
    }

    override fun getItemCount(): Int {
        return variation.options.size
    }


    class ViewHolderColorOption(override val binding: ItemVariationColorsBinding) :
        BaseViewHolder<ItemVariationColorsBinding>(binding)

    class ViewHolderTextOption(override val binding: ItemVariationTextBinding) :
        BaseViewHolder<ItemVariationTextBinding>(binding)


    class ViewHolderImageOption(override val binding: ItemVariationImageBinding) :
        BaseViewHolder<ItemVariationImageBinding>(binding)
}