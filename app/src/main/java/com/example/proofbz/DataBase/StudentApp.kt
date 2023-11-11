package com.example.proofbz.DataBase

import android.app.Application
import android.content.Context
import androidx.room.Room


class StudentApp : Application() {
    private val  STUDENT_DATA_BASE = "student_database"



    val room = Room.databaseBuilder(this, StudentDataBase::class.java,STUDENT_DATA_BASE).build()
}