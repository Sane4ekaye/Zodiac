package com.example.horoscopes

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Html
import android.util.Log
import android.view.*
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.MotionEventCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.*
import kotlinx.android.synthetic.main.layout_opinion_dialog.*
import kotlinx.android.synthetic.main.layout_opinion_dialog.ratingBar3
import kotlinx.android.synthetic.main.layout_opinion_dialog_result.*
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
    var dialog2: Dialog? = null
    var dialog3: Dialog? = null
    var Setting: SharedPreferences? = null
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"

    var zodiac = ""

    var horoscope: Array<String> = arrayOf("Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы")

    // рекламааааааааааааааааааааааааааааа
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "reklama"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMain.visibility = View.INVISIBLE
        likeInfoLayout.visibility = View.INVISIBLE
        textView7.visibility = View.INVISIBLE
        downMenuLayout.visibility = View.INVISIBLE

        val animation = ObjectAnimator.ofFloat(progressBar, "rotation", 0.0f, 360f)
        animation.duration = 1000
        animation.repeatCount = 200
        animation.interpolator = AccelerateDecelerateInterpolator()
        animation.start()
        val timer = object: CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }
            override fun onFinish() {
                if (!isOnline()) {
                    progressBar.visibility = View.INVISIBLE
                    return
                } else {
                    progressBar.visibility = View.INVISIBLE
                    tvMain.visibility = View.VISIBLE
                    likeInfoLayout.visibility = View.VISIBLE
                    textView7.visibility = View.VISIBLE
                    downMenuLayout.visibility = View.VISIBLE
                }
            }
        }
        timer.start()

        if (!isOnline()){
            Toast.makeText(applicationContext, "Нет соединения с интернетом!",Toast.LENGTH_SHORT).show()
            return
        }

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val navigationView: NavigationView = findViewById(R.id.navigationView2)
        navigationView.itemIconTintList = null

        dialog= Dialog(this)
        dialog2= Dialog(this)
        dialog3= Dialog(this)
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

        mToast = Toast(applicationContext)

        getStartText(zodiac, URLHoroscopeTodayTomorrow)

        getForecastToday(zodiac, URLHoroscopeTodayTomorrow)
        getForecastTomorrow(zodiac, URLHoroscopeTodayTomorrow)
        getForecastWeek(zodiac, URLHoroscopeWeekMonth)

        getForecastMonth(zodiac, URLHoroscopeWeekMonth)

    }

    private fun isOnline() : Boolean {
        val cs = Context.CONNECTIVITY_SERVICE
        val cm: ConnectivityManager = getSystemService(cs) as ConnectivityManager
        if (cm.activeNetwork == null) {
            return false
        } else {
            return true
        }
    }

    // это чтобы когда приложение запускалось появлялся прогноз на сегодня сразу
    private fun getStartText(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val resultText = (doc.select("div[itemprop=description]")).select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvMain.text = resultText
            })
        }
    }

    private fun getForecastToday(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac").get()
            val resultText = doc.select("div[itemprop=description]").select("p").text()
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastToday = resultText
            })
        }
    }

    private fun getForecastTomorrow(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url?znak=$zodiac&kn=tomorrow").get()
            val resultText = doc.select("div[itemprop=description]").select("p").text()
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
                resultText.append(textElements.select("p")[i - 1].text() + "\n\n")
                if (i == length) resultText.append(textElements.select("p")[i - 1].text())
            }
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                forecastWeek = resultText.toString()
            })
        }
    }

    private fun getForecastMonth(zodiac: String, url: String) {
        thread {
            val doc = Jsoup.connect("$url/$zodiac/monthly/").get()
            val textElements = doc.select("div[itemprop=articleBody]")
            var divCounter = 0
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
                    var textMonth = "<br><big><sup><b>$text</b></sup></big>"
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

    fun showAd() {
//        MobileAds.initialize(this) {}
//
//        var adRequest = AdRequest.Builder().build()
//
//        // я включил вашу рекламу: ca-app-pub-3940256099942544/1033173712
//        // но вы можете включить тестовую: ca-app-pub-7318343307281487/4358771934
//        InterstitialAd.load(this,"ca-app-pub-7318343307281487/4358771934", adRequest, object : InterstitialAdLoadCallback() {
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Log.i(TAG, adError?.message)
//                mInterstitialAd = null
//            }
//
//            override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                Log.i(TAG, "Ad was loaded.")
//                mInterstitialAd = interstitialAd
//            }
//        })
//
//        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
//            override fun onAdDismissedFullScreenContent() {
//                Log.i(TAG, "Ad was dismissed.")
//            }
//
//            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
//                Log.i(TAG, "Ad failed to show.")
//            }
//
//            override fun onAdShowedFullScreenContent() {
//                Log.i(TAG, "Ad showed fullscreen content.")
//                mInterstitialAd = null
//            }
//        }
//
//        if (mInterstitialAd != null) {
//            mInterstitialAd?.show(this)
//        } else {
//            Log.i(TAG, "The interstitial ad wasn't ready yet.")
//        }
    }

    fun watchToday(view: View) {
        switchColorsForBtnToday()
        tvMain.text = forecastToday
        showAd()
    }

    fun watchTommorow(view: View) {
        switchColorsForBtnTommorow()
        tvMain.text = forecastTomorrow
        showAd()
    }

    fun watchWeek(view: View) {
        switchColorsForBtnWeek()
        tvMain.text = forecastWeek
        showAd()
    }

    fun watchMonth(view: View) {
        switchColorsForBtnMonth()
        tvMain.text = Html.fromHtml(forecastMonth)
        showAd()
    }

    fun setText(name: String){
        getStartText(name, URLHoroscopeTodayTomorrow)
        switchColorsForBtnToday()
        getForecastToday(name, URLHoroscopeTodayTomorrow)
        getForecastTomorrow(name, URLHoroscopeTodayTomorrow)
        getForecastWeek(name, URLHoroscopeWeekMonth)
        getForecastMonth(name, URLHoroscopeWeekMonth)
        val editor: SharedPreferences.Editor = Setting!!.edit()
        editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, name)
        editor.apply()
    }

    fun selSpinner(){
        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selItem = spinner.getItemAtPosition(position).toString()
                if(selItem.equals("Овен") && zodiac != "aries") setText("aries")
                if(selItem.equals("Телец") && zodiac != "taurus") setText("taurus")
                if(selItem.equals("Близнецы") && zodiac != "gemini") setText("gemini")
                if(selItem.equals("Рак") && zodiac != "cancer") setText("cancer")
                if(selItem.equals("Лев") && zodiac != "leo") setText("leo")
                if(selItem.equals("Дева") && zodiac != "virgo") setText("virgo")
                if(selItem.equals("Весы") && zodiac != "libra") setText("libra")
                if(selItem.equals("Скорпион") && zodiac != "scorpio") setText("scorpio")
                if(selItem.equals("Стрелец") && zodiac != "sagittarius") setText("sagittarius")
                if(selItem.equals("Козерог") && zodiac != "capricorn") setText("capricorn")
                if(selItem.equals("Водолей") && zodiac != "aquarius") setText("aquarius")
                if(selItem.equals("Рыбы") && zodiac != "pisces") setText("pisces")
            }
        }
    }
    fun openMenu(view: View) {
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

        if (!isOnline()) {
            btnUpdate.visibility = View.VISIBLE
            spinner.visibility = View.INVISIBLE
        }

        btnUpdate.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        drawerLayout.openDrawer(GravityCompat.START)
        var arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@MainActivity, R.layout.layout_color_spinner, horoscope)
        arrayAdapter.setDropDownViewResource(R.layout.layout_color_spinner2)
        spinner.adapter = arrayAdapter

        if(zodiac == "aries") spinner.setSelection(0)
        else if(zodiac == "taurus") spinner.setSelection(1)
        else if(zodiac == "gemini") spinner.setSelection(2)
        else if(zodiac == "cancer") spinner.setSelection(3)
        else if(zodiac == "leo") spinner.setSelection(4)
        else if(zodiac == "virgo") spinner.setSelection(5)
        else if(zodiac == "libra") spinner.setSelection(6)
        else if(zodiac == "scorpio") spinner.setSelection(7)
        else if(zodiac == "sagittarius") spinner.setSelection(8)
        else if(zodiac == "capricorn") spinner.setSelection(9)
        else if(zodiac == "aquarius") spinner.setSelection(10)
        else if(zodiac == "pisces") spinner.setSelection(11)
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
    }

    fun openDialog(){
        dialog?.setContentView(R.layout.layout_opinion_dialog)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog?.show()
        dialog?.ratingBar3?.setOnRatingBarChangeListener { _, rating, _ ->
            if(rating >=4.0){
                dialog?.hide()
                dialog3?.setContentView(R.layout.layout_opinion_dialog_result_2)
                dialog3?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                dialog3?.show()
                dialog3?.ratingBar3?.rating = rating
            }
            if(rating <4.0){
                dialog?.hide()
                dialog2?.setContentView(R.layout.layout_opinion_dialog_result)
                dialog2?.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                dialog2?.show()
                dialog2?.ratingBar3?.rating = rating
            }
        }

    }

    fun close(view: View) {
        dialog?.hide()
        dialog2?.hide()
        dialog3?.hide()
    }

    fun openOpinion(view: View) {
        dialog?.hide()
        dialog2?.hide()
        dialog3?.hide()
        val intent = Intent(this@MainActivity, FeedBack::class.java)
        startActivity(intent)
    }

    fun buttonLike(view: View) {
        if (isOnline()) openDialog()
    }

    fun buttnoDislike(view: View) {
        if (isOnline()) openDialog()
    }

}
