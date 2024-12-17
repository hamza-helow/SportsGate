package com.souqApp.presentation.notification

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.databinding.FragmentNotificationBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val viewModel: NotificationViewModel by viewModels()
    private lateinit var notificationAdapter: NotificationAdapter

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onResume() {
        super.onResume()
        initAdapter()
        observeToNotifications()
    }

    private fun initAdapter() {
        notificationAdapter = NotificationAdapter()
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        binding.rec.setAdapter(notificationAdapter)
    }


    private fun observeToNotifications() {
        viewModel.notifications.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                notificationAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                notificationAdapter.loadStateFlow.collect { loadStates ->
                    if (loadStates.refresh is LoadState.Loading) {
                        showLoading(true)
                    } else {
                        showLoading(false)
                        binding.tvEmptyState.isVisible(notificationAdapter.itemCount == 0)
                    }
                }
            }
        }
    }

}