package com.asvetenco.intervaltimer.screen.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContent {
            Dashboard()
        }
    }

    @Composable
    fun Content() {
        Scaffold(
            topBar = { AppToolbar() },
            floatingActionButton = { FloatingButton() }
        ) { innerPadding ->
            TimerList(Modifier.padding(innerPadding))
        }
    }

    @Composable
    fun AppToolbar() {
        TopAppBar(
            title = { Text(text = getString(R.string.app_name)) },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = vectorResource(id = R.drawable.ic_baseline_settings_24),
                        tint = Color.White
                    )
                }
            }
        )
    }

    @Composable
    fun FloatingButton() {
        FloatingActionButton(
            onClick = {},
            backgroundColor = Color.Green,
            content = {
                Icon(
                    imageVector = vectorResource(id = R.drawable.ic_baseline_add_24),
                    tint = Color.White
                )
            }
        )
    }

    @Composable
    fun TimerList(modifier: Modifier) {
        LazyColumn(
            modifier = modifier.padding(16.dp)
        ) {
            val list = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14,15)
            items(list) {
                ItemExistingTimer(name = "Timer $it")
            }

        }
    }

    @Composable
    fun ItemExistingTimer(name: String) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Icon(
                    imageVector = vectorResource(id = R.drawable.ic_baseline_sports_24),
                    tint = Color.Magenta
                )
                Text(text = name)
            }
            Divider()
        }
    }

    @Composable
    fun Dashboard() {
        IntervalTimerTheme {
            Surface(color = Color(-0x100)) {
                Content()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Dashboard()
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}