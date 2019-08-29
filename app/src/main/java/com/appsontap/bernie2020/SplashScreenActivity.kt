package com.appsontap.bernie2020

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle




class SplashScreenActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    } 
}