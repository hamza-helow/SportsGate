package com.souqApp.presentation.notification

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.databinding.FragmentNotificationBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.start
import com.souqApp.infra.utils.APP_TAG
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
        binding.rec.layoutManager = LinearLayoutManager(requireContext())
        binding.rec.adapter = notificationAdapter
        viewModel.state.observe(this) { handleState(it) }

    }

    private fun handleState(state: NotificationActivityState) {
        when (state) {
            is NotificationActivityState.Error -> onError(state.throwable)
            is NotificationActivityState.ErrorLoad -> onErrorLoad(state.response)
            is NotificationActivityState.Loaded -> onLoaded(state.notifications)
            is NotificationActivityState.Loading -> onLoading(state.isLoading)
        }
    }

    private fun onLoading(loading: Boolean) {
        binding.progressBar.start(loading)
    }

    private fun onLoaded(notifications: NotificationEntities) {
        notificationAdapter.list = notifications.notifications
        binding.cardNoNotification.isVisible(notifications.notifications.isEmpty())
    }

    private fun onErrorLoad(response: WrappedResponse<NotificationEntities>) {
        showErrorDialog(response.message)
    }

    private fun onError(throwable: Throwable) {
        binding.cardNoNotification.isVisible(true)
        Log.e(APP_TAG, throwable.stackTraceToString())
    }
}