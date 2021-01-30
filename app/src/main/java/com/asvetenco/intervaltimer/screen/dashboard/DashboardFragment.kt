package com.asvetenco.intervaltimer.screen.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.database.domain.Workout
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme
import com.asvetenco.intervaltimer.ui.theme.purple500

class DashboardFragment : Fragment() {


    private lateinit var viewModel: DashboardViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val client = WorkoutProvider(context.applicationContext).localTimerClient()
        viewModel = DashboardViewModel(client)
        viewModel.retrieveWorkouts()
    }
    
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
            val workouts by viewModel.workoutFlow.collectAsState()
            Dashboard(workouts)
        }
    }

    @Composable
    fun Content(workouts: List<Workout>) {
        Scaffold(
            topBar = { AppToolbar() },
            floatingActionButton = { FloatingButton() }
        ) { innerPadding ->
            TimerList(Modifier.padding(innerPadding), workouts)
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
            onClick = ::fillDb,
            backgroundColor = purple500,
            content = {
                Icon(
                    imageVector = vectorResource(id = R.drawable.ic_baseline_add_24),
                    tint = Color.White
                )
            }
        )
    }

    @Composable
    fun TimerList(modifier: Modifier, workouts: List<Workout>) {
        LazyColumn(
            modifier = modifier.padding(16.dp)
        ) {
            items(workouts) {
                ItemExistingTimer(name = "Timer ${it.title}")
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
    fun Dashboard(workouts: List<Workout>) {
        IntervalTimerTheme {
            Surface(color = Color(-0x100)) {
                Content(workouts)
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Dashboard(listOf())
    }

    private fun fillDb() {
        viewModel.fillDb()
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}