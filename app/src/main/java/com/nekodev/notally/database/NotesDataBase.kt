package com.nekodev.notally.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Notes::class], version = 2)
abstract class NotesDataBase : RoomDatabase(){

    abstract fun noteDao() : NotesDao

    companion object{
        @Volatile
        private var INSTANCE : NotesDataBase? = null

        fun getDatabaseInstance(context: Context) : NotesDataBase {
            synchronized(this){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context, NotesDataBase::class.java, "NotesDB")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}