package com.teamenergy.ui.base.utils

import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.res.ResourcesCompat

fun TextView.setClickableText(
    string: String?,
    startIndex: Int = 0,
    endIndex: Int = -1,
    @ColorRes colorRes: Int? = null,
    onTextClick: (() -> Unit)? = null
) {
    val spannableString = SpannableString(string)

    val newEndIndex = if (endIndex == -1) spannableString.length else endIndex

    colorRes?.let {
        val color = ResourcesCompat.getColor(context.resources, colorRes, context.theme)
        spannableString.setSpan(ForegroundColorSpan(color), startIndex, newEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    onTextClick?.let {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) = onTextClick()
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, startIndex, newEndIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        movementMethod = LinkMovementMethod.getInstance()
    }
    text = spannableString
}

fun String.spannableUnderline(start: Int?, end: Int?): SpannableString = SpannableString(this).underline(start, end)

fun String.spannableColor(color: Int, spannableText: String): SpannableString = SpannableString(this).color(color, this.length - spannableText.length, this.length)

fun SpannableString.underline(start: Int?, end: Int?): SpannableString {
    var spanStartPoint = 0
    var spanEndPoint = 0
    if (start != null) spanStartPoint = start
    if (end != null) spanEndPoint = end
    this.setSpan(UnderlineSpan(), spanStartPoint, spanEndPoint, 0)
    return this
}

fun SpannableString.color(color: Int, start: Int?, end: Int?): SpannableString {
    var spanStartPoint = 0
    var spanEndPoint = 0
    if (start != null) spanStartPoint = start
    if (end != null) spanEndPoint = end
    this.setSpan(ForegroundColorSpan(color), spanStartPoint, spanEndPoint, 0)
    return this
}