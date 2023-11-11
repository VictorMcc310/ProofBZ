package com.example.proofbz.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proofbz.DataBase.Dao.StudentDao
import com.example.proofbz.DataBase.Entities.StudentEntity

@Database(entities = [StudentEntity::class], version=1)
abstract class StudentDataBase: RoomDatabase() {

    abstract  fun getStudentDao(): StudentDao
    /*companion object {
        @Volatile
        private var INSTANCE: RoomDatabase? = null
        fun getDatabase(context: Context): RoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,RoomDatabase::class.java,"data").fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }

        }
    }*/
}