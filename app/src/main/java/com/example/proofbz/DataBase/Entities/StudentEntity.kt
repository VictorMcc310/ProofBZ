package com.example.proofbz.DataBase.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "student_table")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo (name = "id") val id :Int =0,
    @ColumnInfo (name = "name") val name :String,
    @ColumnInfo (name = "age")  val age : String
    )