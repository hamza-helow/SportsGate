package com.souqApp.presentation.notification

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.databinding.FragmentNotificationBinding
import com.souqApp.domain.common.BaseResult
import com.souqApp.infra.custome_view.flex_recycler_view.showEmptyState
import com.souqApp.infra.utils.SharedPrefs
import com.souqApp.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment :
    BaseFragment<FragmentNotificationBinding>(FragmentNotificationBinding::inflate) {

    private val viewModel: NotificationViewModel by viewModels()
    private val notificationAdapter = NotificationAdapter()

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onResume() {
        super.onResume()
        observeToLoading()
        observeToNotifications()

        binding.rec.showEmptyState(sharedPrefs.isLogin().not())
    }

    private fun observeToNotifications() {
        viewModel.notificationsLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is BaseResult.Errors -> {
                    onErrorLoad(result.error)
                }

                is BaseResult.Success -> {
                    onLoaded(result.data)
                }
            }
        }
    }

    private fun observeToLoading() {
        viewModel.loadingLiveData.observe(viewLifecycleOwner, ::showLoading)
    }


    private fun onLoaded(entities: NotificationEntities) {
        notificationAdapter.addList(entities.notifications)
        binding.rec.setAdapter(notificationAdapter, LinearLayoutManager(requireContext()))
        binding.rec.showEmptyState(entities.notifications.isEmpty())
    }

    private fun onErrorLoad(response: WrappedResponse<NotificationEntities>) {
        showDialog(response.message)
    }

}