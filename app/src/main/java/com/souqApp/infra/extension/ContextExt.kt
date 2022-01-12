package com.souqApp.infra.extension

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.widget.Toast
import com.souqApp.R


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}

fun Context.showGenericAlertDialog(message: String) {

    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ok") { d, _ -> d.cancel() }
    }.show()
}

var dialog: ProgressDialog? = null

fun Context.showLoader(isShow: Boolean) {

    if (isShow) {
        dialog = ProgressDialog(this)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.setCancelable(false)
        dialog?.show()
        dialog?.setContentView(R.layout.progress_bar)
    } else {
        dialog?.dismiss()
        dialog = null
    }

}

