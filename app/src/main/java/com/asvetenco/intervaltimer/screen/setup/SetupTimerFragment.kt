package com.asvetenco.intervaltimer.screen.setup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.countdown.TimeEvent
import com.asvetenco.intervaltimer.countdown.Workout
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme

class SetupTimerFragment : Fragment() {

    private val timerTitle: String? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getString(
            TIMER_TITLE_KEY
        )
    }
    private val timerId: Long? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getLong(
            TIMER_ID_KEY
        )
    }

    private lateinit var viewModel: SetupTimerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = SetupTimerViewModel(
            WorkoutProvider(context.applicationContext).localTimerClient()
        )
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
            SetupContent()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.retrieveTimerById(timerId)
    }

    @Composable
    fun SetupContent() {
        IntervalTimerTheme {
            Surface(color = Color(-0x100)) {
                Scaffold(
                    topBar = { AppToolbar() },
                ) { innerPadding -> SetUpTimer(Modifier.padding(innerPadding)) }
            }
        }
    }

    @Composable
    fun AppToolbar() {
        TopAppBar(
            title = { Text(text = timerTitle ?: stringResource(R.string.app_name)) },
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
    fun SetUpTimer(modifier: Modifier) {
        val workout = viewModel.workout.collectAsState()

        Column(
            modifier = modifier.padding(16.dp)
        ) {
            workout.value.events.forEach { ItemSetUp(it, workout.value) }
        }
    }

    @Composable
    fun ItemSetUp(event: TimeEvent, workout: Workout) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(event.title, modifier = Modifier.weight(1f, true))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, true)
            ) {
                ChangeQuantityButton(
                    vectorResource(R.drawable.ic_baseline_remove_24),
                    onClick = { viewModel.minus(event, workout) })
                Text(event.time.toString(), modifier = Modifier.padding(start = 16.dp, end = 16.dp))
                ChangeQuantityButton(
                    vectorResource(R.drawable.ic_baseline_add_24),
                    onClick = { viewModel.plus(event, workout) })
            }
        }
    }

    @Composable
    fun ChangeQuantityButton(icon: ImageVector, onClick: () -> Unit) {
        OutlinedButton(
            onClick = onClick,
            shape = CircleShape
        ) {
            Icon(icon)
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SetupContent()
    }

    companion object {
        private const val TIMER_TITLE_KEY = "com.asvetenco.intervaltimer.screen.setup.timer_title"
        private const val TIMER_ID_KEY = "com.asvetenco.intervaltimer.screen.setup.timer_id"

        fun newInstance(title: String? = null, id: Long? = null) = SetupTimerFragment().apply {
            arguments = bundleOf(TIMER_TITLE_KEY to title, TIMER_ID_KEY to id)
        }
    }

}