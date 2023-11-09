package com.example.catchthemoment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Получение значения чекбокса из SharedPreferences
            val sharedPreferences = context.getSharedPreferences("YourPreferences", Context.MODE_PRIVATE)
            val startOnBoot = sharedPreferences.getBoolean("startOnBoot", false)

            if (startOnBoot) {
                // Запуск Foreground Service
                val serviceIntent = Intent(context, CatchMomentService::class.java)
                ContextCompat.startForegroundService(context, serviceIntent)
            }
        }
    }
}
