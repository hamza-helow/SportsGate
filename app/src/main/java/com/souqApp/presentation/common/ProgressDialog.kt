package com.souqApp.presentation.common

import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import java.lang.Exception


class ProgressDialog(private val context: Context) {
    private val dialog: ProgressDialog = ProgressDialog(context)
    private fun showProgressDialog() {
        try {
            dialog.show()
            dialog.setCanceledOnTouchOutside(false)
            dialog.setContentView(com.souqApp.R.layout.progress_bar)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showLoader(show: Boolean) {
        if (!dialog.isShowing && show)
            showProgressDialog()
        else
            dialog.dismiss()
    }


}