package com.example.micamion2

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.WorkerParameters
import java.util.Calendar
import java.util.concurrent.TimeUnit


class TimeCheckWorker(appContext: Context, workerParams: WorkerParameters)
    : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        // Update the theme via ViewModel
        DarkModeViewModel().updateTheme()
        return Result.success()
    }
}
fun scheduleTimeCheck(context: Context) {
    val workRequest = OneTimeWorkRequestBuilder<TimeCheckWorker>()
        .setInitialDelay(calculateTimeUntil6pm(), TimeUnit.MILLISECONDS)
        .build()


    WorkManager.getInstance(context).enqueue(workRequest)
}

fun calculateTimeUntil6pm(): Long {
    val now = Calendar.getInstance()
    val target = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 18)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    // If it's already past 6pm on the same day, target the next day's 6pm
    if (now.after(target)) {
        target.add(Calendar.DAY_OF_MONTH, 1)
    }

    return target.timeInMillis - now.timeInMillis
}

