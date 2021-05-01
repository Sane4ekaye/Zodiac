package com.example.horoscopes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewParent
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val URLHoroscope: String = "https://1001goroskop.ru"

    private var forecastToday = ""
    private var forecastTomorrow = ""
    private var forecastTodayWeek = ""
    private var forecastTodayMonth = ""

    var horoscope: Array<String> = arrayOf("Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы")

    var Setting: SharedPreferences? = null
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        var navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.itemIconTintList = null

        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        val zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

        getStartText(zodiac, URLHoroscope)

        getForecastToday(zodiac, URLHoroscope)
        getForecastTomorrow(zodiac, "tomorrow", URLHoroscope)
        //spinner.adapter = ArrayAdapter<String>(this@MainActivity, R.layout.layout_color_spinner, R.array.horoscope)

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

    // это заточка
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
        drawerLayout.openDrawer(GravityCompat.START)
        var arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@MainActivity, R.layout.layout_color_spinner, horoscope)
        arrayAdapter.setDropDownViewResource(R.layout.layout_color_spinner2)
        spinnernegra.adapter = arrayAdapter

        spinnernegra?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selItem = spinnernegra.getItemAtPosition(position).toString()
                if(selItem.equals("Овен"))
                    // добавь сюда
                    getStartText("aries", URLHoroscope)
                    getForecastToday("aries", URLHoroscope)
                    getForecastTomorrow("aries", "tomorrow", URLHoroscope)
                if(selItem.equals("Телец"))
                    getStartText("taurus", URLHoroscope)
                    getForecastToday("taurus", URLHoroscope)
                    getForecastTomorrow("taurus", "tomorrow", URLHoroscope)
            }
        }
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

    fun feedback(view: View) {
        val intent = Intent(this@MainActivity, FeedBack::class.java)
        startActivity(intent)
    }

}


