package com.example.horoscopes

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
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

    private val URLHoroscopeTodayTomorrow: String = "https://1001goroskop.ru"
    private val URLHoroscopeWeekMonth: String = "https://horoscopes.rambler.ru"

    private var forecastToday = ""
    private var forecastTomorrow = ""
    private var forecastWeek = ""
    private var forecastMonth = ""

    var Setting: SharedPreferences? = null
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"

    var zodiac = ""

    var horoscope: Array<String> = arrayOf("Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        var navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.itemIconTintList = null
        //spinner.adapter = ArrayAdapter<String>(this@MainActivity, R.layout.layout_color_spinner, R.array.horoscope)

        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()
        
        getStartText(zodiac, URLHoroscopeTodayTomorrow)

        getForecastToday(zodiac, URLHoroscopeTodayTomorrow)
        getForecastTomorrow(zodiac, URLHoroscopeTodayTomorrow)
        getForecastWeek(zodiac, URLHoroscopeWeekMonth)

        getForecastMonth(zodiac, URLHoroscopeWeekMonth)


    }

    // это затычка
    private fun getStartText(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val resultText = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvMain.text = resultText
            })
        }
    }

    private fun getForecastToday(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val resultText = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastToday = resultText
            })
        }
    }

    private fun getForecastTomorrow(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac&kn=tomorrow").get()
            val textElements = doc.select("div[itemprop=description]")
            val resultText = textElements.select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastTomorrow = resultText
            })
        }
    }

    private fun getForecastWeek(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url/$zodiac/weekly/").get()
            val textElements = doc.select("div[itemprop=articleBody]")
            var length = textElements.select("p").size
            var resultText = StringBuilder()
            for (i in 1..length) {
                resultText.append(textElements.select("p")[i-1].text() + "\n\n")
                if (i == length) resultText.append(textElements.select("p")[i-1].text())
            }
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastWeek = resultText.toString()
            })
        }
    }

    private fun getForecastMonth(zodiac: String, url: String) {
        thread {
        // TODO если ты захочешь убиться нахуй, то вот затычка хуйня и залупа но решение
        //  var negri = textElements.html()

            val doc = Jsoup.connect("$url/$zodiac/monthly/").get()
            val textElements = doc.select("div[itemprop=articleBody]")

            var divCounter = 0
            var negri = textElements.select("div")[divCounter + 1].nextElementSibling().text()

            var lengthP = textElements.select("p").size
            var lengthH2 = textElements.select("h2").size
            var length = lengthP + lengthH2 + 1
            var resultText = StringBuilder()
            var tag = "p"
            var pCounter = 0
            var h2Counter = 0
            var text = ""
            for (i in 1..length) {
                if (i == 1) {
                    resultText.append(textElements.select("p")[0].text() + "<br><br>")
                    text = textElements.select("p")[0].nextElementSibling().text()
                    tag = textElements.select("p")[0].nextElementSibling().nodeName()
                }
                if (tag.equals("p")) {
                    resultText.append(text + "<br><br>")
                } else if (tag.equals("h2")) {
                    var textMonth = "<big><b>$text</b></big>"
                    resultText.append(textMonth + "<br>")
                } else if (tag.equals("div")) {
                    text = textElements.select("div")[divCounter + 1].nextElementSibling().text()
                    tag = textElements.select("div")[divCounter + 1].nextElementSibling().nodeName()
                    divCounter += 5
                    continue
                }
                if (i != length) {
                    var tempText = text
                    text = doc.selectFirst("$tag:containsOwn($text)").nextElementSibling().text()
                    tag = doc.selectFirst("$tag:containsOwn($tempText)").nextElementSibling().tagName()
                }

                // -----------------------------
//                if (myTag.equals("p")) {
//                    resultText.append(textElements.select(myTag)[pCounter].text() + "\n\n")
//                    pCounter++
//                    myTag = textElements.select(myTag)[pCounter].nextElementSibling().nodeName()
//                } else if (myTag.equals("h2")) {
//                    var textTemp = textElements.select(myTag)[h2Counter].text()
//                    var textMonth = "<big>$textTemp</big>"
//                    resultText.append(textMonth + "\n")
//                    h2Counter++
//                    myTag = textElements.select(myTag)[h2Counter].nextElementSibling().nodeName()
//                }
              }
//            var lengthP = textElements.select("p").size
//            var lengthH2 = textElements.select("h2").size
//            var length = lengthP + lengthH2
//            var resultText = StringBuilder()
//            var myTag = ""
//            for (i in 1..length) {
//                if (textElements.select("p")[0].nextElementSibling().nodeName().equals("p")) {
//                    resultText.append(textElements.select("p")[i - 1].text() + "\n\n")
//                    myTag = "p"
//                } else if (textElements.select("p")[i - 1].nextElementSibling().nodeName().equals("h2")) {
//                    var textTemp = textElements.select("p")[i - 1].text()
//                    var textMonth = "<big>$textTemp</big>"
//                    resultText.append(textMonth + "\n")
//                    myTag = "h2"
//                }
//
//                if (myTag.equals("p")) {
//                    resultText.append(textElements.select("p")[i - 1].text() + "\n\n")
//                }
//
//                if (i == length) resultText.append(textElements.select("p")[i-1].text())
//            }
//            var length = textElements.select("p").size
//            var resultText = StringBuilder()
//            for (i in 1..length) {
//                resultText.append(textElements.select("p")[i-1].text() + "\n\n")
//                if (i == length) resultText.append(textElements.select("p")[i-1].text())
//            }
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastMonth = resultText.toString()
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
        tvMain.text = forecastWeek
    }

    fun watchMonth(view: View) {
        switchColorsForBtnMonth()
        tvMain.text = Html.fromHtml(forecastMonth)
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
                if(selItem.equals("Дева"))
                  //zodiac = "aries"
//                getStartText("aries", URLHoroscope)
                  getForecastToday("virgo", URLHoroscopeTodayTomorrow)
//                getForecastTomorrow("aries", "tomorrow", URLHoroscope)
//                if(selItem.equals("Телец"))
//                    getStartText("taurus", URLHoroscope)
//                getForecastToday("taurus", URLHoroscope)
//                getForecastTomorrow("taurus", "tomorrow", URLHoroscope)
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
