package com.asvetenco.intervaltimer.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.asvetenco.intervaltimer.ui.theme.purple50
import com.asvetenco.intervaltimer.ui.theme.purple700

@Composable
fun AppToolbar(
    titleText: String = "",
    title: @Composable () -> Unit = { Text(text = titleText, color = purple700) },
    @DrawableRes iconRes: Int,
    action: () -> Unit = { }
) {
    TopAppBar(
        title = title,
        backgroundColor = purple50,
        elevation = 0.dp,
        actions = {
            IconButton(onClick = { action() }) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = null,
                    tint = purple700
                )
            }
        }
    )
}
