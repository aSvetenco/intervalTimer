package com.asvetenco.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.FtsOptions
import androidx.room.PrimaryKey

@Entity(tableName = "Lap")
internal data class LapEntity(
    @PrimaryKey
    val id: Long,
    val number: Int,
    val timerId: Long,
    val warmUp: Int,
    val coolDawn: Int,
    val work: Int,
    val rest: Int,
    val intervalCount: Int
)
