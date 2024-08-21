package com.example.quotes.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.quotes.R
import com.example.quotes.activities.home.HomeActivity
import com.example.quotes.activities.login.LoginActivity
import com.example.quotes.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val intent:Intent = if(FirebaseAuth.getInstance().currentUser==null){
            Intent(this, LoginActivity::class.java)
        }else{
            Intent(this, HomeActivity::class.java)
        }
        Handler().postDelayed(Runnable {
            startActivity(intent)
            finish()
        },2000)


    }

}