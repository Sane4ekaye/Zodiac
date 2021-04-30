package com.example.horoscopes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {

    private val URLHoroscope: String = "https://1001goroskop.ru"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getText("gemini")
    }
    private fun getText(zodiac: String){
        thread {
            val doc = Jsoup.connect("$URLHoroscope?znak=$zodiac").get()
            val textElements = doc.select("div[itemprop=description]")
            val text123 = textElements.select("p").text()
            //val textEnd = doc.getElementById("description")
            this@MainActivity.runOnUiThread(java.lang.Runnable {
                tvToday.text = text123
            })
        }
    }
}