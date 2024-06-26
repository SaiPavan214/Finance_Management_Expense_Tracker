package com.example.financemanagementapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Person
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import java.time.LocalDate
import java.time.ZoneId

object NotificationScheduler {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnspecifiedImmutableFlag")
    fun scheduleNotification(context: Context, transactionId: Long, isDebt: Boolean, dateOfRepayment: LocalDate, person: String, amount: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !isNotificationPermissionGranted(context)
        ) {
            // If notification permission is not granted, handle the request outside this method
            // This should be done in an activity, not in a utility class like this.
            return
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("transactionId", transactionId)
            putExtra("isDebt", isDebt)
            putExtra("person", person)
            putExtra("amount", amount)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context.applicationContext,
            transactionId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = dateOfRepayment.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent)
    }

    private fun isNotificationPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }
}
