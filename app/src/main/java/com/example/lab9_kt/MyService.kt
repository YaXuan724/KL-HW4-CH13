package com.example.lab9_kt

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.*
import kotlinx.coroutines.delay as coroutinesDelay

class MyService : Service() {
    private var channel = ""
    private lateinit var thread: Thread
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let {
            channel = it.getString("channel", "")
        }
        GlobalScope.launch (Dispatchers.Main){
            broadcast(
                when(channel){
                    "music" -> "歡迎來到音樂頻道"
                    "new" -> "歡迎來到新聞頻道"
                    "sport" -> "歡迎來到體育頻道"
                    else -> "頻道錯誤"
                }
            )
            if (::thread.isInitialized && thread.isAlive)
                thread.interrupt()
            coroutinesDelay(timeMillis = 3000)
            thread = Thread{
                try {
                    broadcast(
                        when(channel){
                            "music" -> "即將播放本月TOP10音樂"
                            "new" -> "即將為您提供獨家新聞"
                            "sport" -> "即將為您播報本周NBA賽事"
                            else -> "頻道錯誤"
                        }
                    )
                }catch (e: InterruptedException){
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        return START_STICKY
    }
    override fun onBind(intent: Intent): IBinder?= null

    private fun broadcast(msg: String) = sendBroadcast(Intent(channel).putExtra("msg",msg))


}