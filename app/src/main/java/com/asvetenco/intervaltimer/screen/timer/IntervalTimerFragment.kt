package com.asvetenco.intervaltimer.screen.timer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.countdown.CountdownService
import com.asvetenco.intervaltimer.countdown.CountdownTimer
import com.asvetenco.intervaltimer.data.TimeEventMapper
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700

class IntervalTimerFragment : Fragment() {

    private val workoutId: Long? by lazy(LazyThreadSafetyMode.NONE) {
        arguments?.getLong(
            TIMER_ID_KEY
        )
    }

    private lateinit var viewModel: IntervalTimerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = IntervalTimerViewModel(
            WorkoutProvider(context.applicationContext).localTimerClient(),
            TimeEventMapper(),
            CountdownService(CountdownTimer(), lifecycleScope)
        )
        viewModel.retrieveTimerById(workoutId)
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

    @Composable
    fun SetupContent() {
        IntervalTimerTheme {
            Content()
        }
    }

    @Composable
    fun Content() {

        val event = viewModel.eventTitle.collectAsState()
        val onTick = viewModel.remainingTime.collectAsState()

        ConstraintLayout(
            modifier = Modifier
                .background(purple50)
                .fillMaxSize()
        ) {
            val (closeIcon, timer, hint, controls) = createRefs()
            Icon(
                vectorResource(id = R.drawable.ic_baseline_cancel_24),
                tint = purple700,
                modifier = Modifier
                    .clickable(onClick = { viewModel.pauseWorkout() })
                    .padding(16.dp)
                    .constrainAs(closeIcon) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            Text(
                text = onTick.value.toString(),
                fontSize = 96.sp,
                modifier = Modifier.constrainAs(timer) {
                    top.linkTo(parent.top)
                    bottom.linkTo(hint.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Text(text = stringResource(id = event.value.title),
                fontSize = 32.sp,
                modifier = Modifier.constrainAs(hint) {
                    top.linkTo(timer.bottom)
                    bottom.linkTo(controls.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            Controls(
                Modifier
                    .constrainAs(controls) {
                        top.linkTo(hint.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }

    @Composable
    fun Controls(modifier: Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            InteractiveIcon(R.drawable.ic_baseline_replay_5_24, Modifier.weight(1f)) {}
            InteractiveIcon(R.drawable.ic_baseline_fast_rewind_24, Modifier.weight(1f)) {}
            IconButton(
                { viewModel.startWorkout() },
                Modifier.weight(2f),
            ) {
                Icon(
                    vectorResource(id = R.drawable.ic_baseline_play_arrow_24),
                    tint = purple700
                )
            }
            InteractiveIcon(R.drawable.ic_baseline_fast_forward_24, Modifier.weight(1f)) {}
            InteractiveIcon(R.drawable.ic_baseline_volume_up_24, Modifier.weight(1f)) {

            }
        }
    }

    @Composable
    private fun InteractiveIcon(
        @DrawableRes iconRes: Int,
        modifier: Modifier,
        action: () -> Unit
    ) {
        IconButton(
            onClick = { /*TODO*/ },
            modifier = modifier
        ) {
            Icon(
                vectorResource(id = iconRes),
                tint = purple700
            )
        }

    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SetupContent()
    }

    companion object {
        private const val TIMER_ID_KEY = "com.asvetenco.intervaltimer.screen.timer.timer_id"

        fun newInstance(id: Long? = null) = IntervalTimerFragment().apply {
            arguments = bundleOf(TIMER_ID_KEY to id)
        }
    }
}