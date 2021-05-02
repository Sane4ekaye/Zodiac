package com.example.horoscopes

import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_feed_back.*


class FeedBack : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed_back)

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
    }

    fun openMenu(view: View) {}
}