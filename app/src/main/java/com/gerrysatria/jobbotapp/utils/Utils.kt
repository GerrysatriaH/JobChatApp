package com.gerrysatria.jobbotapp.utils

import android.content.Context
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gerrysatria.jobbotapp.R
import java.text.SimpleDateFormat
import java.util.Locale

fun ProgressBar.show(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun RecyclerView.show(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun TextView.show(state: Boolean) {
    visibility = if (state) View.VISIBLE else View.GONE
}

fun showToast(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
}

fun showDialog(context: Context, title: String, message: String){
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(context.getString(R.string.oke)){ dialog, _ ->
            dialog.dismiss()
        }.show()
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date!!)
    } catch (_: Exception) { dateString }
}

fun dialogDeleteAction(context: Context, title: String, message: String, positiveAction: () -> Unit){
    MaterialAlertDialogBuilder(context)
        .setTitle(title)
        .setMessage(message)
        .setNegativeButton(context.getString(R.string.no)){ dialog, _ ->
            dialog.dismiss()
        }.setPositiveButton(context.getString(R.string.yes)) { _, _ ->
            positiveAction.invoke()
        }.show()
}