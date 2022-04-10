package com.example.plana.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.plana.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_getStarted.setOnClickListener {
            val intent = Intent(this, OverviewActivity::class.java)
            startActivity(intent)
        }

        middle_yellow_image.setOnClickListener {
            val intent = Intent(this, DetailActivity::class.java)

            startActivity(intent)
        }

        val intent = Intent(this, OverviewActivity::class.java)
        startActivity(intent)
    }

}