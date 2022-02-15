package com.nekodev.notally.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Notes")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Int ,

    val title : String,

    val body: String,

    val date: String
    )