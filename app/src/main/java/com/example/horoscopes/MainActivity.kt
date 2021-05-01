package com.example.horoscopes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val URLHoroscope: String = "https://1001goroskop.ru"

    private var forecastToday = ""
    private var forecastTomorrow = ""
    private var forecastTodayWeek = ""
    private var forecastTodayMonth = ""

    var Setting: SharedPreferences? = null
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

//        if(Setting!!.contains(APP_PREFERENCES_SELECTED_HOROSCOPE)){
//            if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("aries")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("taurus")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("gemini")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("cancer")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("leo")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("virgo")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("libra")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("scorpio")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("sagittarius")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("capricorn")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else if(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").equals("pisces")) getText(Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString())
//            else tvMain.text = "error text!"
//        }


        getStartText(zodiac, URLHoroscope)

        getForecastToday(zodiac, URLHoroscope)
        getForecastTomorrow(zodiac, "tomorrow", URLHoroscope)
    }

    private fun getText(zodiac: String) {
        thread {
            val doc = Jsoup.connect("$URLHoroscope?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvMain.text = text123
            })
        }
    }

    // это затычка
    private fun getStartText(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvMain.text = text123
            })
        }
    }

    private fun getForecastToday(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastToday = text123
            })
        }
    }

    private fun getForecastTomorrow(zodiac: String, date: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac&kn=$date").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastTomorrow = text123
            })
        }
    }

    fun watchToday(view: View) {
        switchColorsForBtnToday()
        tvMain.text = forecastToday
    }

    fun watchTommorow(view: View) {
        switchColorsForBtnTommorow()
        tvMain.text = forecastTomorrow
    }

    fun watchWeek(view: View) {
        switchColorsForBtnWeek()
//        getForecast("aries", "week", "https://1001goroskop.ru")
    }

    fun watchMonth(view: View) {
        switchColorsForBtnMonth()
    }

    fun openMenu(view: View) {
        //TODO тут короче сделать шобы данные стирались и мы заново выбирали знак зодиака
    }

    private fun switchColorsForBtnToday() {
        btnToday.setBackgroundResource(R.drawable.button)
        btnToday.setTextColor(Color.WHITE)

        btnTodayDown.setBackgroundResource(R.drawable.button)
        btnTodayDown.setTextColor(Color.WHITE)
        // остальные белыми
        btnTommorow.setBackgroundResource(R.color.white_true)
        btnTommorow.setTextColor(getColor(R.color.blueApp))

        btnTommorowDown.setBackgroundResource(R.color.white_true)
        btnTommorowDown.setTextColor(getColor(R.color.blueApp))
        //
        btnWeek.setBackgroundResource(R.color.white_true)
        btnWeek.setTextColor(getColor(R.color.blueApp))

        btnWeekDown.setBackgroundResource(R.color.white_true)
        btnWeekDown.setTextColor(getColor(R.color.blueApp))
        //
        btnMonth.setBackgroundResource(R.color.white_true)
        btnMonth.setTextColor(getColor(R.color.blueApp))

        btnMonthDown.setBackgroundResource(R.color.white_true)
        btnMonthDown.setTextColor(getColor(R.color.blueApp))
    }

    private fun switchColorsForBtnTommorow() {
        btnTommorow.setBackgroundResource(R.drawable.button)
        btnTommorow.setTextColor(Color.WHITE)

        btnTommorowDown.setBackgroundResource(R.drawable.button)
        btnTommorowDown.setTextColor(Color.WHITE)

        // остальные белыми
        btnToday.setBackgroundResource(R.color.white_true)
        btnToday.setTextColor(getColor(R.color.blueApp))

        btnTodayDown.setBackgroundResource(R.color.white_true)
        btnTodayDown.setTextColor(getColor(R.color.blueApp))
        //
        btnWeek.setBackgroundResource(R.color.white_true)
        btnWeek.setTextColor(getColor(R.color.blueApp))

        btnWeekDown.setBackgroundResource(R.color.white_true)
        btnWeekDown.setTextColor(getColor(R.color.blueApp))
        //
        btnMonth.setBackgroundResource(R.color.white_true)
        btnMonth.setTextColor(getColor(R.color.blueApp))

        btnMonthDown.setBackgroundResource(R.color.white_true)
        btnMonthDown.setTextColor(getColor(R.color.blueApp))
    }

    private fun switchColorsForBtnWeek() {
        btnWeek.setBackgroundResource(R.drawable.button)
        btnWeek.setTextColor(Color.WHITE)

        btnWeekDown.setBackgroundResource(R.drawable.button)
        btnWeekDown.setTextColor(Color.WHITE)

        // остальные белыми
        btnToday.setBackgroundResource(R.color.white_true)
        btnToday.setTextColor(getColor(R.color.blueApp))

        btnTodayDown.setBackgroundResource(R.color.white_true)
        btnTodayDown.setTextColor(getColor(R.color.blueApp))
        //
        btnTommorow.setBackgroundResource(R.color.white_true)
        btnTommorow.setTextColor(getColor(R.color.blueApp))

        btnTommorowDown.setBackgroundResource(R.color.white_true)
        btnTommorowDown.setTextColor(getColor(R.color.blueApp))
        //
        btnMonth.setBackgroundResource(R.color.white_true)
        btnMonth.setTextColor(getColor(R.color.blueApp))

        btnMonthDown.setBackgroundResource(R.color.white_true)
        btnMonthDown.setTextColor(getColor(R.color.blueApp))
    }

    private fun switchColorsForBtnMonth() {
        btnMonth.setBackgroundResource(R.drawable.button)
        btnMonth.setTextColor(Color.WHITE)

        btnMonthDown.setBackgroundResource(R.drawable.button)
        btnMonthDown.setTextColor(Color.WHITE)

        // остальные белыми
        btnToday.setBackgroundResource(R.color.white_true)
        btnToday.setTextColor(getColor(R.color.blueApp))

        btnTodayDown.setBackgroundResource(R.color.white_true)
        btnTodayDown.setTextColor(getColor(R.color.blueApp))
        //
        btnTommorow.setBackgroundResource(R.color.white_true)
        btnTommorow.setTextColor(getColor(R.color.blueApp))

        btnTommorowDown.setBackgroundResource(R.color.white_true)
        btnTommorowDown.setTextColor(getColor(R.color.blueApp))
        //
        btnWeek.setBackgroundResource(R.color.white_true)
        btnWeek.setTextColor(getColor(R.color.blueApp))

        btnWeekDown.setBackgroundResource(R.color.white_true)
        btnWeekDown.setTextColor(getColor(R.color.blueApp))
    }

}
