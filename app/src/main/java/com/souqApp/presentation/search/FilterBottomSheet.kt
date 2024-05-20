package com.souqApp.presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.souqApp.BR
import com.souqApp.R
import com.souqApp.databinding.BottomSheetFilterBinding
import com.souqApp.databinding.ItemFilterBinding
import com.souqApp.infra.utils.A_TO_Z
import com.souqApp.infra.utils.BEST_MATCH
import com.souqApp.infra.utils.BEST_SELLING
import com.souqApp.infra.utils.BaseRecyclerAdapter
import com.souqApp.infra.utils.NEW_ARRIVAL
import com.souqApp.infra.utils.PRICE_HIGH_TO_LOW
import com.souqApp.infra.utils.PRICE_LOW_TO_HIGH
import com.souqApp.infra.utils.Z_TO_A

class FilterBottomSheet : BottomSheetDialogFragment() {

    private lateinit var filters: List<Filter>

    private lateinit var binding: BottomSheetFilterBinding
    private val filterAdapter = FilterAdapter()
    lateinit var onSelectItem: (Filter) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSubmit.setOnClickListener {
            dismiss()
            onSelectItem(filterAdapter.list[filterAdapter.selectedItem])
        }

        filters = listOf(
            Filter(context?.getString(R.string.new_arrivals_str) ?: "", NEW_ARRIVAL),
            Filter("Ascending product name A-Z", A_TO_Z),
            Filter("Descending product name Z-A", Z_TO_A),
            Filter(context?.getString(R.string.price_low_to_high_str) ?: "", PRICE_LOW_TO_HIGH),
            Filter(context?.getString(R.string.price_high_to_low_str) ?: "", PRICE_HIGH_TO_LOW),
            Filter(context?.getString(R.string.best_selling_str) ?: "", BEST_SELLING),
            Filter(context?.getString(R.string.best_match_str) ?: "", BEST_MATCH),
        )

        filterAdapter.list = filters
        binding.rec.layoutManager = LinearLayoutManager(context)
        binding.rec.adapter = filterAdapter

    }

}


class FilterAdapter : BaseRecyclerAdapter<ItemFilterBinding, Filter>() {

    var selectedItem = 0

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.name = getItemByPosition(position).name
        holder.itemView.setOnClickListener {
            selectedItem = holder.layoutPosition
            notifyDataSetChanged()
        }

        holder.bind(BR.selected , selectedItem == position)
    }

    override fun getBinding(parent: ViewGroup, viewType: Int): ItemFilterBinding {
        return ItemFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun enableAddItem(): Boolean {
        return false
    }

    override fun needLoadMore(page: Int) {
    }

}

data class Filter(
    val name: String,
    val type: Int
)