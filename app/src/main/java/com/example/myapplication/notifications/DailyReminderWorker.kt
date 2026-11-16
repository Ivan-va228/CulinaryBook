package com.example.myapplication.notifications

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.content.Context

class DailyReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        // Check notification permission
        if (
            Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }

        // Create notification channel
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = android.app.NotificationChannel(
                "recipe_reminders",
                "Daily recipe reminders",
                android.app.NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as android.app.NotificationManager

            manager.createNotificationChannel(channel)
        }

        // Create notification
        val notification = NotificationCompat.Builder(
            applicationContext,
            "recipe_reminders"
        )
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Recipe Book")
            .setContentText("Discover a new recipe today!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        // Show notification
        NotificationManagerCompat.from(applicationContext)
            .notify(1, notification)

        return Result.success()
    }
}