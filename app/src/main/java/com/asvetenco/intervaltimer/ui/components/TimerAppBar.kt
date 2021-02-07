package com.asvetenco.intervaltimer.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700


@Composable
fun AppToolbar(
    title: String,
    @DrawableRes iconRes: Int,
    action: () -> Unit = { }
) {
    TopAppBar(
        title = { Text(text = title, color = purple700) },
        backgroundColor = purple50,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { action() }) {
                Icon(
                    imageVector = vectorResource(id = iconRes),
                    tint = purple700
                )
            }
        }
    )
}