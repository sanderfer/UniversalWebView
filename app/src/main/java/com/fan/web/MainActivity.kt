package com.fan.web

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val url =
            "http://58.42.249.209:17953/DATA/10051001/202002/20200227/8a3a62ae-86c9-4a9e-ac48-9ba876a8ba8d/8a3a62ae-86c9-4a9e-ac48-9ba876a8ba8d.pdf"
        mainWeb.load(url)
    }
}
