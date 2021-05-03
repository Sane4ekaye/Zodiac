package com.example.horoscopes

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_feed_back.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_navigation_header.*


class FeedBack : AppCompatActivity() {
    var Setting: SharedPreferences? = null
    var zodiac = ""
    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"
    var horoscope: Array<String> = arrayOf("Овен", "Телец", "Близнецы", "Рак", "Лев", "Дева", "Весы", "Скорпион", "Стрелец", "Козерог", "Водолей", "Рыбы")
    var mToast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        mToast = Toast(applicationContext)

        editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (editTextEmail.text.isEmpty() || editTextQuestion.text.isEmpty()) {
                    buttonSend.setBackgroundResource(R.drawable.button_feedback_1)
                    buttonSend.setTextColor(getColor(R.color.feedback_button_false))
                } else {
                    buttonSend.setBackgroundResource(R.drawable.button_feedback_2)
                    buttonSend.setTextColor(getColor(R.color.feedback_button_true))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        editTextQuestion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (editTextEmail.text.isEmpty() || editTextQuestion.text.isEmpty()) {
                    buttonSend.setBackgroundResource(R.drawable.button_feedback_1)
                    buttonSend.setTextColor(getColor(R.color.feedback_button_false))
                } else {
                    buttonSend.setBackgroundResource(R.drawable.button_feedback_2)
                    buttonSend.setTextColor(getColor(R.color.feedback_button_true))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        buttonSend.setOnClickListener(){
            if(!editTextEmail.text.isEmpty() && !editTextQuestion.text.isEmpty()){
                var inflater: LayoutInflater = layoutInflater
                var customToastLayout: View = inflater.inflate(R.layout.layout_toast, findViewById(R.id.root_layout))

                mToast!!.duration = Toast.LENGTH_LONG
                mToast!!.view = customToastLayout
                mToast!!.show()
                finish()
            }
        }
        btnMenu2.setOnClickListener(){
            Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
            zodiac = Setting!!.getString(APP_PREFERENCES_SELECTED_HOROSCOPE, "").toString()

            drawerLayout.openDrawer(GravityCompat.START)
            var arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(this@FeedBack, R.layout.layout_color_spinner, horoscope)
            arrayAdapter.setDropDownViewResource(R.layout.layout_color_spinner2)
            spinner.adapter = arrayAdapter
//
//            if(zodiac == "aries")
//                spinner.setSelection(0)
//            else if(zodiac == "taurus")
//                spinner.setSelection(1)
//            else if(zodiac == "gemini")
//                spinner.setSelection(2)
//            else if(zodiac == "cancer")
//                spinner.setSelection(3)
//            else if(zodiac == "leo")
//                spinner.setSelection(4)
//            else if(zodiac == "virgo")
//                spinner.setSelection(5)
//            else if(zodiac == "libra")
//                spinner.setSelection(6)
//            else if(zodiac == "scorpio")
//                spinner.setSelection(7)
//            else if(zodiac == "sagittarius")
//                spinner.setSelection(8)
//            else if(zodiac == "capricorn")
//                spinner.setSelection(9)
//            else if(zodiac == "aquarius")
//                spinner.setSelection(10)
//            else if(zodiac == "pisces")
//                spinner.setSelection(11)
//            else Toast.makeText(this, "Произошла ошибка!", Toast.LENGTH_SHORT).show()
//
//            selSpinner()
        }
    }

}