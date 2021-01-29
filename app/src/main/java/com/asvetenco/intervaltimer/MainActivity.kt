package com.asvetenco.intervaltimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asvetenco.intervaltimer.screen.dashboard.DashboardFragment

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                    R.id.host_fragment,
                    DashboardFragment.newInstance(),
                    DashboardFragment::class.java.simpleName
                ).commit()
        }
    }
}