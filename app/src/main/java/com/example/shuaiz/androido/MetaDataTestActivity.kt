package com.example.shuaiz.androido

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStreamReader

class MetaDataTestActivity : AppCompatActivity() {

    @BindView(R.id.meta_data_result)
    lateinit var resultTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meta_data_test)
        ButterKnife.bind(this)
    }

    private fun getStringFromMainfest(applicationInfo: ApplicationInfo) = "\"export_activity\": ${applicationInfo.metaData.getString("export_activity")}"

    private fun getArrayMetaDataFromStringString(applicationInfo: ApplicationInfo): String {
        val arrayId = applicationInfo.metaData.getInt("export_activity_array")
        return "\"export_activity_array\": ${resources.getStringArray(arrayId).joinToString()}"
    }

    private fun getJsonMetaDataFromRaw(applicationInfo: ApplicationInfo): String {
        val json = applicationInfo.metaData.getInt("export_json")
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(json)))
        return "\"export_activity_json\": ${reader.use { it.readText() }}"
    }

    private fun getXmlMetaDataFromXml(applicationInfo: ApplicationInfo): String {
        val xml = applicationInfo.metaData.getInt("export_xml")
        val xrp = resources.getXml(xml)
        var result = ""
        try {
            var eventType = xrp.getEventType()
            while (eventType !== XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val tagName = xrp.name
                        if (tagName != null && tagName == "item") {
                            val name = xrp.getAttributeValue(null, "name")
                            val alias = xrp.getAttributeValue(null, "alias")
                            result += "\"export_activity_json\": name: $name, alias: $alias"
                        }
                    }
                    else -> {
                    }
                }
                eventType = xrp.next()
            }
        } catch (e: Exception) {
        }
        return result
    }

    private fun getMetaDataApplicationInfo(): ApplicationInfo {
        val packageManager = packageManager
        return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }

    @OnClick(R.id.btn_meta_string)
    fun fromMainfest(): Unit {
        resultTextView.text = getStringFromMainfest(getMetaDataApplicationInfo())
    }

    @OnClick(R.id.btn_meta_string_array)
    fun fromArray(): Unit {
        resultTextView.text = getArrayMetaDataFromStringString(getMetaDataApplicationInfo())
    }

    @OnClick(R.id.btn_meta_string_json)
    fun fromJson(): Unit {
        resultTextView.text = getJsonMetaDataFromRaw(getMetaDataApplicationInfo())
    }

    @OnClick(R.id.btn_meta_string_xml)
    fun fromXml(): Unit {
        resultTextView.text = getXmlMetaDataFromXml(getMetaDataApplicationInfo())
    }
}
