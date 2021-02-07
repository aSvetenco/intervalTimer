package com.asvetenco.intervaltimer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.asvetenco.intervaltimer.screen.dashboard.DashboardFragment
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setBottomBar()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.host_fragment,
                DashboardFragment.newInstance(),
                DashboardFragment::class.java.simpleName
            ).commit()
        }
    }

    private fun setBottomBar() {
        findViewById<ComposeView>(R.id.bottom_bar).setContent {
            Scaffold(
                backgroundColor = purple50,
                bottomBar = {
                    BottomAppBar(
                        backgroundColor = purple50,
                        cutoutShape = RoundedCornerShape(36.dp)
                    ) {
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = vectorResource(id = R.drawable.ic_baseline_sports_24),
                                    tint = purple700
                                )
                            },
                            selected = true,
                            onClick = { })
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    imageVector = vectorResource(id = R.drawable.ic_baseline_sports_24),
                                    tint = purple700
                                )
                            },
                            selected = false,
                            onClick = { })
                    }
                }
            ) {

            }
        }
    }
}