package com.souqApp.presentation.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.souqApp.R
import com.souqApp.data.common.utlis.WrappedListResponse
import com.souqApp.data.common.utlis.WrappedResponse
import com.souqApp.data.notification.remote.NotificationEntities
import com.souqApp.data.notification.remote.NotificationEntity
import com.souqApp.databinding.ActivityNotificationBinding
import com.souqApp.infra.extension.isVisible
import com.souqApp.infra.extension.setup
import com.souqApp.infra.extension.showGenericAlertDialog
import com.souqApp.infra.extension.start
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()
    private val notificationAdapter = NotificationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rec.layoutManager = LinearLayoutManager(this)
        binding.rec.adapter = notificationAdapter

        //setup tool bar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setup(showTitleEnabled = true)
        supportActionBar?.title = getString(R.string.notifications_str)


        viewModel.state.observe(this, { handleState(it) })

    }

    private fun handleState(state: NotificationActivityState?) {
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

        showGenericAlertDialog(response.formattedErrors())

    }

    private fun onError(throwable: Throwable) {

        Log.e("ERer", throwable.stackTraceToString())

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}