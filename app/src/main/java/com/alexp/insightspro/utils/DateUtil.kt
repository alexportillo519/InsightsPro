package com.alexp.insightspro.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.text.format.DateUtils
import com.alexp.insightspro.R
import org.w3c.dom.Comment
import java.text.SimpleDateFormat
import java.util.*

class DateUtil {

    @SuppressLint("SimpleDateFormat")
    private val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

    fun formatTime(jsonTime: String?): String {
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time = sdf.parse(jsonTime.toString())?.time
        val now = System.currentTimeMillis()
        return DateUtils.getRelativeTimeSpanString(time!!, now, DateUtils.MINUTE_IN_MILLIS).toString()
    }
}