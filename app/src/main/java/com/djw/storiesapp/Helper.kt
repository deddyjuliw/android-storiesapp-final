package com.djw.storiesapp

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.TextView
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.math.min

fun View.loadAnim(isVisible: Boolean, duration: Long = 500){
    ObjectAnimator
        .ofFloat(this, View.ALPHA, if (isVisible) 1f else 0f)
        .setDuration(duration)
        .start()
}

@SuppressLint("SimpleDateFormat")
fun withDateFormat(timestamp: String): String? {
    try {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            .parse(timestamp)?.let {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                    .format(it)
            }
        return formattedDate
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return null
    //val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
    //val date = format.parse(timestamp) as Date
    //val formattedDate = DateFormat.getDateInstance(DateFormat.FULL).format(date)
    //this.text = formattedDate
}