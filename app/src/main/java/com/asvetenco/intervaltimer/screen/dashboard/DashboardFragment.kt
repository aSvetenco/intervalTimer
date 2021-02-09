package com.asvetenco.intervaltimer.screen.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.data.Workout
import com.asvetenco.intervaltimer.screen.setup.SetupTimerFragment
import com.asvetenco.intervaltimer.screen.timer.IntervalTimerFragment
import com.asvetenco.intervaltimer.ui.components.AppToolbar
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme
import com.asvetenco.intervaltimer.ui.theme.purple100
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700

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
            backgroundColor = purple50,
            topBar = {
                AppToolbar(
                    stringResource(id = R.string.app_name),
                    R.drawable.ic_baseline_add_24
                ) {
                    showSetUpFragment()
                }
            }
        ) { innerPadding ->
            TimerList(Modifier.padding(innerPadding), workouts)
        }
    }

    @Composable
    fun TimerList(modifier: Modifier, workouts: List<Workout>) {
        LazyColumn(
            modifier = modifier.background(purple50)
        ) {
            items(workouts) {
                ItemExistingTimer(it)
            }
        }
    }

    @Composable
    fun ItemExistingTimer(workout: Workout) {
        Column(
            modifier = Modifier.clickable(onClick = { showTimerFragment(workout) }),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(Modifier.padding(top = 16.dp, bottom = 16.dp, end = 8.dp, start = 8.dp)) {
                Icon(
                    modifier = Modifier.padding(start = 16.dp),
                    imageVector = vectorResource(id = R.drawable.ic_baseline_sports_24),
                    tint = purple700
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .weight(1f),
                    text = workout.title
                )
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .clickable(onClick = { showSetUpFragment(workout) }),
                    imageVector = vectorResource(id = R.drawable.ic_baseline_edit_24),
                    tint = purple700
                )
            }
            Divider(
                Modifier.padding(start = 8.dp, end = 8.dp),
                purple100
            )
        }
    }

    @Composable
    fun Dashboard(workouts: List<Workout>) {
        IntervalTimerTheme {
            Surface { Content(workouts) }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        Dashboard(
            listOf(
                Workout(title = "Tabata"),
                Workout(title = "Round"),
                Workout(title = "Heavy"),
            )
        )
    }

    private fun showSetUpFragment(workout: Workout? = null) {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.host_fragment,
                SetupTimerFragment.newInstance(workout?.title, workout?.id),
                SetupTimerFragment::class.java.simpleName
            )
            addToBackStack(SetupTimerFragment::class.java.simpleName)
            commit()
        }
    }

    private fun showTimerFragment(workout: Workout? = null) {
        parentFragmentManager.beginTransaction().apply {
            replace(
                R.id.host_fragment,
                IntervalTimerFragment.newInstance(workout?.id),
                IntervalTimerFragment::class.java.simpleName
            )
            addToBackStack(IntervalTimerFragment::class.java.simpleName)
            commit()
        }
    }

    companion object {
        fun newInstance() = DashboardFragment()
    }
}