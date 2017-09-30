package com.example.shuaiz.androido

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ToggleButton
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

class NotificationActivity : AppCompatActivity() {

    private val NOTIFICATION_ID_1 = 1001
    private val NOTIFICATION_ID_2 = 1002
    private val NOTIFICATION_CHANNEL_ID: String = "notification_channel_id"
    private val NOTIFICATION_CHANNEL_NAME: String = "Notification Channel Name"

    @BindView(R.id.notification_content)
    lateinit var notificationEdit: EditText
    @BindView(R.id.lockscreen_toggle)
    lateinit var lockscreenToggle: ToggleButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        ButterKnife.bind(this)

        val notifyMgr = getNotificationManager()
//        notifyMgr.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID)
        notifyMgr.createNotificationChannel(createNotificationChannel())
    }

    @OnClick(R.id.btn_send1, R.id.btn_send2)
    fun sendNotification(btn: Button) {
        when (btn.id) {
            R.id.btn_send1 -> notify(getNotification(notificationEdit.text.toString(), "Hello1"), NOTIFICATION_ID_1)
            R.id.btn_send2 -> notify(getNotification(notificationEdit.text.toString(), "Hello2"), NOTIFICATION_ID_2)
        }
    }

    fun notify(builder: NotificationCompat.Builder, id: Int) {
        val notifyMgr = getNotificationManager()
        notifyMgr.notify(id, builder.build())
    }

    private fun createNotificationChannel(): NotificationChannel {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
        when (lockscreenToggle.isChecked) {
            true -> notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            false -> notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        }
        return notificationChannel
    }

    private fun getNotification(title: String, body: String): NotificationCompat.Builder {
        return NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true)
    }

    private fun getSmallIcon(): Int = android.R.drawable.stat_notify_chat

    private fun getNotificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}
