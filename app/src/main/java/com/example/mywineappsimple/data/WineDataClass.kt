package com.example.mywineappsimple.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wines")
data class WineDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String = "",
    val year: Int = 0,
    val quantity: Int = 0,
    val color: String = "",
    val taste: String = ""
)