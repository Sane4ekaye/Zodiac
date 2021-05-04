package com.nomercheck.horoscope1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_feed_back.*


class FeedBack : AppCompatActivity() {
    var mToast: Toast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)
        mToast = Toast(applicationContext)
        drawerlayout2.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val navigationView: NavigationView = findViewById(R.id.navigationView2)
        navigationView.itemIconTintList = null

        FBEditTextEmail.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (FBEditTextEmail.text.isEmpty() || FBeditTextQuestion.text.isEmpty()) {
                    FBbuttonSend.setBackgroundResource(R.drawable.button_feedback_1)
                    FBbuttonSend.setTextColor(getColor(R.color.feedback_button_false))
                } else {
                    FBbuttonSend.setBackgroundResource(R.drawable.button_feedback_2)
                    FBbuttonSend.setTextColor(getColor(R.color.feedback_button_true))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        FBeditTextQuestion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (FBEditTextEmail.text.isEmpty() || FBeditTextQuestion.text.isEmpty()) {
                    FBbuttonSend.setBackgroundResource(R.drawable.button_feedback_1)
                    FBbuttonSend.setTextColor(getColor(R.color.feedback_button_false))
                } else {
                    FBbuttonSend.setBackgroundResource(R.drawable.button_feedback_2)
                    FBbuttonSend.setTextColor(getColor(R.color.feedback_button_true))
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        FBbuttonSend.setOnClickListener(){
            if(!FBEditTextEmail.text.isEmpty() && !FBeditTextQuestion.text.isEmpty()){
                var inflater: LayoutInflater = layoutInflater
                var customToastLayout: View = inflater.inflate(R.layout.layout_toast, findViewById(R.id.root_layout))

                mToast!!.duration = Toast.LENGTH_LONG
                mToast!!.view = customToastLayout
                mToast!!.show()
                finish()
            }
        }
    }
    fun feedbackMenu(view: View) {
        drawerlayout2.openDrawer(GravityCompat.START)
    }

}