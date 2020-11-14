package com.todorant.android.util

import android.view.View
import android.widget.TextView

import com.google.android.material.snackbar.Snackbar

import androidx.annotation.ColorInt

object AlertUtils {

    fun showSnackbar(view: View,
                     callback: Snackbar.Callback?,
                     @ColorInt backgroundColor: Int,
                     @ColorInt textColor: Int,
                     text: String,
                     @ColorInt actionTextColor: Int,
                     actionText: String,
                     onClickListener: View.OnClickListener?) {
        var onClickListener = onClickListener
        if (onClickListener == null) {
            onClickListener = View.OnClickListener { }
        }
        val snackbar = Snackbar
                .make(view, text, Snackbar.LENGTH_LONG)
                .addCallback(callback!!)
                .setActionTextColor(actionTextColor)
                .setAction(actionText, onClickListener)
        val sbView = snackbar.view
        sbView.setBackgroundColor(backgroundColor)
        val textView = sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(textColor)
        snackbar.show()
    }

}
