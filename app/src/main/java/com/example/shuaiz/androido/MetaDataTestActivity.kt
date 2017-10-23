package com.example.shuaiz.androido

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.example.shuaiz.androido.model.UIModel
import com.example.shuaiz.androido.model.UI
import com.google.gson.Gson
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

    private fun getStringFromManifest(applicationInfo: ApplicationInfo) = "\"export_activity\": ${applicationInfo.metaData.getString("export_activity")}"

    private fun getSplitStringFromManifest(applicationInfo: ApplicationInfo): String {
        val splitString = applicationInfo.metaData.getString("export_string_split")
        val uiGroups = splitString.split(";")
        val uiList = uiGroups.map {
            val uiData = it.trim().split("|")
            val activity = UIModel(uiData[0].trim(), uiData[1].trim(), uiData[2].trim())
            activity
        }
        return "\"export_activity_split\": \n$uiList"
    }

    private fun getArrayMetaDataFromStringString(applicationInfo: ApplicationInfo): String {
        val arrayId = applicationInfo.metaData.getInt("export_activity_array")
        return "\"export_activity_array\": ${resources.getStringArray(arrayId).joinToString()}"
    }

    private fun getJsonMetaDataFromRaw(applicationInfo: ApplicationInfo): String {
        val json = applicationInfo.metaData.getInt("export_json")
        val reader = BufferedReader(InputStreamReader(resources.openRawResource(json)))
        val jsonData = reader.use { it.readText() }
        val fromJson = Gson().fromJson(jsonData, UI::class.java)
        return "\"export_activity_json\": \n$fromJson"
    }

    private fun getJsonInlineMetaDataFromMainfest(applicationInfo: ApplicationInfo): String {
        val jsonData = applicationInfo.metaData.getString("export_json_inline")
        val fromJson = Gson().fromJson(jsonData, UI::class.java)
        return "\"export_activity_json_inline\": \n$fromJson"
    }

    private fun getXmlMetaDataFromXml(applicationInfo: ApplicationInfo): String {
        val xml = applicationInfo.metaData.getInt("export_xml")
        val xrp = resources.getXml(xml)
        val activities = mutableListOf<UIModel>()
        try {
            var eventType = xrp.getEventType()
            while (eventType !== XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        val tagName = xrp.name
                        if (tagName != null && tagName == "item") {
                            val name = xrp.getAttributeValue(null, "name")
                            val alias = xrp.getAttributeValue(null, "alias")
                            val type = xrp.getAttributeValue(null, "type")
                            activities.add(UIModel(name, alias, type))
                        }
                    }
                    else -> {
                    }
                }
                eventType = xrp.next()
            }
        } catch (e: Exception) {
        }
        return "\"export_activity_xml\": \n${UI(activities)}"
    }

    private fun getMetaDataApplicationInfo(): ApplicationInfo {
        val packageManager = packageManager
        return packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
    }

    @OnClick(R.id.btn_meta_string)
    fun fromManifest(): Unit {
        resultTextView.text = getStringFromManifest(getMetaDataApplicationInfo())
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

    @OnClick(R.id.btn_meta_string_json_inline)
    fun fromJsonInline(): Unit {
        resultTextView.text = getJsonInlineMetaDataFromMainfest(getMetaDataApplicationInfo())
    }

    @OnClick(R.id.btn_meta_split)
    fun fromManifestSplit(): Unit {
        resultTextView.text = getSplitStringFromManifest(getMetaDataApplicationInfo())
    }
}
