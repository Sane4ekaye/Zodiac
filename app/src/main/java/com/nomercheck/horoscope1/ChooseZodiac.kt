package com.nomercheck.horoscope1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choose_zodiac.*

class ChooseZodiac : AppCompatActivity() {

    val APP_PREFERENCES: String = "horoscope"
    val APP_PREFERENCES_SELECTED_HOROSCOPE: String = "selectedHoroscope"
    var isSelectedHoroscope: Boolean? = null
    var Setting: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_zodiac)
        Setting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

        if(Setting!!.contains(APP_PREFERENCES_SELECTED_HOROSCOPE)) {
            val intent = Intent(this@ChooseZodiac, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            isSelectedHoroscope = false
        }


        // ============ Buttons ============ //

        buttonOven?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("aries")
            }
        }
        buttonTaurus?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("taurus")
            }
        }
        buttonTwins?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("gemini")
            }
        }
        buttonCancer?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("cancer")
            }
        }
        buttonLion?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("leo")
            }
        }
        buttonVirgin?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("virgo")
            }
        }
        buttonScales?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("libra")
            }
        }
        buttonScorpio?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("scorpio")
            }
        }
        buttonSagittarius?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("sagittarius")
            }
        }
        buttonCapricorn?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("capricorn")
            }
        }
        buttonAquarius?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("aquarius")
            }
        }
        buttonFish?.setOnClickListener{
            if(isSelectedHoroscope == false){
                putSharedPreference("pisces")
            }
        }

        // ================================ //
    }

    fun putSharedPreference(horoscope: String){
        var editor: SharedPreferences.Editor = Setting!!.edit()
        editor.putString(APP_PREFERENCES_SELECTED_HOROSCOPE, horoscope)
        editor.apply()
        addIntent()
    }
    fun addIntent(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}