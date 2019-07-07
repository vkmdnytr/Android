package com.example.sosyalyazilim.ui.login

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import com.example.sosyalyazilim.R
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private var mUsername: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signInButton.setOnClickListener {
            if(isEnterTheUserName()){
                goToMessageFragment()
            }
        }
    }


    private fun goToMessageFragment() {
        val intent = Intent()
        intent.putExtra("userName", mUsername)
        intent.putExtra("id", 1)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
    private fun isEnterTheUserName():Boolean {
        // Reset errors.
        usernameEditText.error = null
        val username = usernameEditText.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(username)) {
            usernameEditText.error = getString(R.string.error_field_required)
            usernameEditText.requestFocus()
            return false
        }
        mUsername = username
        return true
    }

}
