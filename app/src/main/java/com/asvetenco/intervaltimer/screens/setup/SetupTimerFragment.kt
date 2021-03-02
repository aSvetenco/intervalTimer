package com.asvetenco.intervaltimer.screens.setup

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.asvetenco.database.WorkoutProvider
import com.asvetenco.intervaltimer.R
import com.asvetenco.intervaltimer.data.Exercise
import com.asvetenco.intervaltimer.data.TimeEvent
import com.asvetenco.intervaltimer.data.TimeEventMapper
import com.asvetenco.intervaltimer.data.Workout
import com.asvetenco.intervaltimer.lazily
import com.asvetenco.intervaltimer.ui.components.AppToolbar
import com.asvetenco.intervaltimer.ui.theme.IntervalTimerTheme
import com.asvetenco.intervaltimer.ui.theme.purple200
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700
import kotlinx.coroutines.flow.collect

class SetupTimerFragment : Fragment() {

    private val timerId: Long? by lazily { arguments?.getLong(TIMER_ID_KEY) }

    private lateinit var viewModel: SetupTimerViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel = SetupTimerViewModel(
            WorkoutProvider(context.applicationContext).localTimerClient(),
            TimeEventMapper()
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
        lifecycleScope.launchWhenResumed {
            viewModel.onWorkoutSaved.collect {
                showToast(
                    getString(R.string.set_up_timer_was_saved, it)
                )
            }
        }
    }

    @Composable
    fun SetupContent() {
        val focusRequester = remember { FocusRequester() }
        val workout = viewModel.workout.collectAsState()
        IntervalTimerTheme {
            Surface(color = purple50) {
                Scaffold(
                    topBar = {
                        AppToolbar(
                            title = { EditTimerTitle(focusRequester, workout.value) },
                            iconRes = R.drawable.ic_baseline_save_24
                        ) {
                            viewModel.saveWorkout()
                        }
                    }
                ) { innerPadding -> SetUpTimer(workout.value, Modifier.padding(innerPadding)) }
            }
        }
    }

    @Composable
    private fun TextFieldHint(hint: String) {
        Text(hint, color = purple200)
    }

    @Composable
    fun SetUpTimer(
        workout: Workout,
        modifier: Modifier
    ) {
        LazyColumn(
            modifier = modifier.padding(16.dp)
        ) {
            items(workout.events.size) {
                ItemSetUp(workout.events[it], workout)
            }
        }
    }

    @Composable
    fun ItemSetUp(event: TimeEvent, workout: Workout) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(stringResource(id = event.title), modifier = Modifier.weight(1f, true))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f, true)
                    .border(1.dp, color = purple700, RectangleShape)
            ) {
                ChangeQuantityButton(
                    R.drawable.ic_baseline_remove_24,
                    onClick = { viewModel.minus(event, workout) })

                if (event is Exercise) {
                    EditExercises(event, workout)
                } else {
                    EditMinutes(event, workout)
                    Text(text = ":", modifier = Modifier.background(Color.Yellow))
                    EditSeconds(event, workout)
                }

                ChangeQuantityButton(
                    R.drawable.ic_baseline_add_24,
                    onClick = { viewModel.plus(event, workout) })
            }
        }
    }

    @Composable
    fun ChangeQuantityButton(@DrawableRes iconRes: Int, onClick: () -> Unit) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.background(Color.Blue).size(36.dp)
        ) { Icon(painterResource(iconRes), null) }
    }

    @Composable
    private fun EditExercises(event: TimeEvent, workout: Workout) {
        EditTimeValue(timeValue = event.time, false) {
            viewModel.editExercises(event, workout, it)
        }
    }

    @Composable
    private fun EditMinutes(event: TimeEvent, workout: Workout) {
        EditTimeValue(timeValue = event.minutes) {
            viewModel.editMinutes(event, workout, it)
        }
    }

    @Composable
    private fun EditSeconds(event: TimeEvent, workout: Workout) {
        EditTimeValue(timeValue = event.seconds) {
            viewModel.editSeconds(event, workout, it)
        }
    }

    @Composable
    private fun EditTimerTitle(
        focusRequester: FocusRequester,
        workout: Workout
    ) {
        TextField(
            value = workout.title,
            onValueChange = viewModel::onWorkoutTitleEdited,
            modifier = Modifier.focusRequester(focusRequester),
            label = {
                if (workout.title.isEmpty()) TextFieldHint(
                    stringResource(
                        R.string.set_up_timer_enter_title
                    )
                )
            }
        )
    }

    @Composable
    private fun EditTimeValue(
        timeValue: Int,
        isEnable: Boolean = true,
        onValueChange: (String) -> Unit = {}
    ) {
        BasicTextField(
            value = timeValue.toString(),
            textStyle = TextStyle(color = purple700, fontSize = 16.sp),
            onValueChange = { onValueChange(it) },
            enabled = isEnable,
            modifier = Modifier
                .size(24.dp)
                .background(Color.Green),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            cursorBrush = SolidColor(purple700),
        )
    }


    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SetupContent()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val TIMER_ID_KEY = "com.asvetenco.intervaltimer.screen.setup.timer_id"

        fun newInstance(id: Long? = null) = SetupTimerFragment().apply {
            arguments = bundleOf(TIMER_ID_KEY to id)
        }
    }

}