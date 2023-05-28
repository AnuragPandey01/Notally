package xyz.droidev.notally.data.local.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "Notes")
data class Notes(

    @PrimaryKey(autoGenerate = true)
    val id: Int = -1 ,

    val title : String,

    val body: String,

    val date: String,

    val created_at: String
    ) : Parcelable