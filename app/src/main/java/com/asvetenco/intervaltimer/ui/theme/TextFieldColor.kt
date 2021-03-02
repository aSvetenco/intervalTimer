package com.asvetenco.intervaltimer.ui.theme

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun EditTimeColor(): TextFieldColors =
    TextFieldDefaults.textFieldColors(
        textColor = purple700,
        cursorColor = purple700,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

