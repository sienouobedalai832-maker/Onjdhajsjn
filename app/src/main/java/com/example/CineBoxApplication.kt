package com.example

import android.app.Application
import android.content.Intent
import kotlin.system.exitProcess

class CineBoxApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
            val intent = Intent(this, ErrorActivity::class.java).apply {
                putExtra("error", exception.stackTraceToString())
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(1)
        }
    }
}
