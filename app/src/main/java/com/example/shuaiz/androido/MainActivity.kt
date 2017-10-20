package com.example.shuaiz.androido

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import butterknife.ButterKnife
import butterknife.OnClick


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
    }

    @OnClick(R.id.btn_notification_channel)
    internal fun notificationOnClick() {
        startActivity(Intent(this, NotificationActivity::class.java))
    }

    @OnClick(R.id.btn_meta_data)
    internal fun metadataOnClick() {
        startActivity(Intent(this, MetaDataTestActivity::class.java))
    }
}
