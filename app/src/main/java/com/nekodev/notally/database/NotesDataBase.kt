package com.nekodev.notally.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Notes::class], version = 3)
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
                        .addMigrations(
                            object: Migration(2,3){
                                override fun migrate(database: SupportSQLiteDatabase) {
                                    database.execSQL("ALTER TABLE Notes ADD created_at TEXT NOT NULL DEFAULT '' ")
                                    database.execSQL("Update Notes SET created_at = date Where created_at = '' ")
                                }
                            }
                        )
                        .build()
                }
            }
            return INSTANCE!!
        }
    }

}