package com.asvetenco.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Timer")
internal data class TimerEntity(
    @PrimaryKey
    val id: Long,
    val title: String
)
