package com.example.horoscopes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.*
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.*
import kotlinx.android.synthetic.main.layout_opinion_dialog.*
import kotlinx.android.synthetic.main.layout_toast.*
import org.jsoup.Jsoup
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val URLHoroscopeTodayTomorrow: String = "https://1001goroskop.ru"
    private val URLHoroscopeWeekMonth: String = "https://horoscopes.rambler.ru"

    private var forecastToday = ""
    private var forecastTomorrow = ""
    private var forecastWeek = ""
    private var forecastMonth = ""

    var mToast: Toast? = null
    var dialog: Dialog? = null
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
        dialog= Dialog(this)
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

        mToast = Toast(applicationContext)
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
            //val resultText = (doc.select("div[itemprop=description]")).select("p").text()  <---- В одну строку (хуй знает работает или нет, но ошибок не дает)
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
        // TODO если ты захочешь убиться нахуй, то вот затoчка хуйня и залупа но решение
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
            var text = ""
            for (i in 1..length) {
                if (i == 1) {
                    resultText.append(textElements.select("p")[0].text() + "<br><br>")
                    text = textElements.select("p")[0].nextElementSibling().text()
                    tag = textElements.select("p")[0].nextElementSibling().nodeName()
                }
                if (tag.equals("p") && i == length) {
                    resultText.append(text + "")
                } else if (tag.equals("p")) {
                    resultText.append(text + "<br><br>")
                } else if (tag.equals("h2")) {
                    var textMonth = "<big><sup><b>$text</b></sup></big>"
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
              }

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

    fun selSpinner(){
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selItem = spinner.getItemAtPosition(position).toString()

                if(selItem.equals("Овен") && zodiac != "aries") {
                    getStartText("aries", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("aries", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("aries", URLHoroscopeTodayTomorrow)
                    getForecastWeek("aries", URLHoroscopeWeekMonth)
                    getForecastMonth("aries", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "aries")
                    editor.apply()
                }
                if(selItem.equals("Телец") && zodiac != "taurus"){
                    getStartText("taurus", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("taurus", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("taurus", URLHoroscopeTodayTomorrow)
                    getForecastWeek("taurus", URLHoroscopeWeekMonth)
                    getForecastMonth("taurus", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "taurus")
                    editor.apply()
                }
                if(selItem.equals("Близнецы") && zodiac != "gemini"){
                    getStartText("gemini", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("gemini", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("gemini", URLHoroscopeTodayTomorrow)
                    getForecastWeek("gemini", URLHoroscopeWeekMonth)
                    getForecastMonth("gemini", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "gemini")
                    editor.apply()
                }
                if(selItem.equals("Рак") && zodiac != "cancer"){
                    getStartText("cancer", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("cancer", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("cancer", URLHoroscopeTodayTomorrow)
                    getForecastWeek("cancer", URLHoroscopeWeekMonth)
                    getForecastMonth("cancer", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "cancer")
                    editor.apply()
                }
                if(selItem.equals("Лев") && zodiac != "leo"){
                    getStartText("leo", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("leo", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("leo", URLHoroscopeTodayTomorrow)
                    getForecastWeek("leo", URLHoroscopeWeekMonth)
                    getForecastMonth("leo", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "leo")
                    editor.apply()
                }
                if(selItem.equals("Дева") && zodiac != "virgo"){
                    getStartText("virgo", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("virgo", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("virgo", URLHoroscopeTodayTomorrow)
                    getForecastWeek("virgo", URLHoroscopeWeekMonth)
                    getForecastMonth("virgo", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "virgo")
                    editor.apply()
                }
                if(selItem.equals("Весы") && zodiac != "libra"){
                    getStartText("libra", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("libra", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("libra", URLHoroscopeTodayTomorrow)
                    getForecastWeek("libra", URLHoroscopeWeekMonth)
                    getForecastMonth("libra", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "libra")
                    editor.apply()
                }
                if(selItem.equals("Скорпион") && zodiac != "scorpio"){
                    getStartText("scorpio", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("scorpio", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("scorpio", URLHoroscopeTodayTomorrow)
                    getForecastWeek("scorpio", URLHoroscopeWeekMonth)
                    getForecastMonth("scorpio", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "scorpio")
                    editor.apply()
                }
                if(selItem.equals("Стрелец") && zodiac != "sagittarius"){
                    getStartText("sagittarius", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("sagittarius", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("sagittarius", URLHoroscopeTodayTomorrow)
                    getForecastWeek("sagittarius", URLHoroscopeWeekMonth)
                    getForecastMonth("sagittarius", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "sagittarius")
                    editor.apply()
                }
                if(selItem.equals("Козерог") && zodiac != "capricorn"){
                    getStartText("capricorn", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("capricorn", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("capricorn", URLHoroscopeTodayTomorrow)
                    getForecastWeek("capricorn", URLHoroscopeWeekMonth)
                    getForecastMonth("capricorn", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "capricorn")
                    editor.apply()
                }
                if(selItem.equals("Водолей") && zodiac != "aquarius"){
                    getStartText("aquarius", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("aquarius", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("aquarius", URLHoroscopeTodayTomorrow)
                    getForecastWeek("aquarius", URLHoroscopeWeekMonth)
                    getForecastMonth("aquarius", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "aquarius")
                    editor.apply()
                }
                if(selItem.equals("Рыбы") && zodiac != "pisces"){
                    getStartText("pisces", URLHoroscopeTodayTomorrow)
                    switchColorsForBtnToday()
                    getForecastToday("pisces", URLHoroscopeTodayTomorrow)
                    getForecastTomorrow("pisces", URLHoroscopeTodayTomorrow)
                    getForecastWeek("pisces", URLHoroscopeWeekMonth)
                    getForecastMonth("pisces", URLHoroscopeWeekMonth)
                    var editor: SharedPreferences.Editor = Setting!!.edit()
                    editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, "pisces")
                    editor.apply()
                }
            }
        }
    }
    fun openMenu(view: View) {
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

        drawerLayout.openDrawer(GravityCompat.START)
        var arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@MainActivity, R.layout.layout_color_spinner, horoscope)
        arrayAdapter.setDropDownViewResource(R.layout.layout_color_spinner2)
        spinner.adapter = arrayAdapter

        if(zodiac == "aries")
            spinner.setSelection(0)
        else if(zodiac == "taurus")
            spinner.setSelection(1)
        else if(zodiac == "gemini")
            spinner.setSelection(2)
        else if(zodiac == "cancer")
            spinner.setSelection(3)
        else if(zodiac == "leo")
            spinner.setSelection(4)
        else if(zodiac == "virgo")
            spinner.setSelection(5)
        else if(zodiac == "libra")
            spinner.setSelection(6)
        else if(zodiac == "scorpio")
            spinner.setSelection(7)
        else if(zodiac == "sagittarius")
            spinner.setSelection(8)
        else if(zodiac == "capricorn")
            spinner.setSelection(9)
        else if(zodiac == "aquarius")
            spinner.setSelection(10)
        else if(zodiac == "pisces")
            spinner.setSelection(11)
        else Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_SHORT).show()

        selSpinner()

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
        drawerLayout.closeDrawer(GravityCompat.START)
        val intent = Intent(this@MainActivity, FeedBack::class.java)
        startActivity(intent)
    }

    fun like(view: View) {
        drawerLayout.closeDrawer(GravityCompat.START)
        openDialog()

//        var builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
//        var viewGroup: ViewGroup = findViewById(android.R.id.content)
//        var dialogView: View = LayoutInflater.from(view.context).inflate(R.layout.layout_opinion_dialog, viewGroup, false)
//        builder.setView(dialogView)
//        var alertDialog: AlertDialog = builder.create()
//        alertDialog.show()

//        var inflater: LayoutInflater = layoutInflater
//        var customToastLayout: View = inflater.inflate(R.layout.layout_toast, findViewById(R.id.root_layout))
//
//        mToast!!.duration = Toast.LENGTH_LONG
//        mToast!!.view = customToastLayout
//        mToast!!.show()
    }

    fun openDialog(){
        dialog?.setContentView(R.layout.layout_opinion_dialog)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.show()
    }

    fun close(view: View) {
        dialog?.hide()
    }

    fun openOpinion(view: View) {
        dialog?.hide()
        val intent = Intent(this@MainActivity, FeedBack::class.java)
        startActivity(intent)
    }


}
