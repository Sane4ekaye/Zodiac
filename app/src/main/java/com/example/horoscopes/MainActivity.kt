package com.example.horoscopes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val URLHoroscope: String = "https://1001goroskop.ru"
    var Setting: SharedPreferences? = null
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        if(Setting!!.contains(APP_PREFERENCES_SELECTED_HOROSCOPE)){
            if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("aries")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("taurus")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("gemini")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("cancer")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("leo")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("virgo")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("libra")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("scorpio")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("sagittarius")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("capricorn")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("pisces")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
            else tvToday.text = "error text!"
        }
    }

    private fun getText(zodiac: String) {
        thread {
            val doc = Jsoup.connect("$URLHoroscope?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvToday.text = text123
            })
        }
    }

    fun watchTommorow(view: View) {
        btnTommorow.setBackgroundResource(R.drawable.button)
        btnTommorow.setTextColor(Color.WHITE)
    }
}
