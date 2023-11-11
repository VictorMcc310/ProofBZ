package com.example.proofbz.DataBase.Dao

import androidx.room.*
import com.example.proofbz.DataBase.Entities.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {

    @Query("SELECT * FROM student_table")
     fun getAllStudents():List<StudentEntity>

    @Query("SELECT * FROM student_table WHERE id = :id")
    fun getStudent(id: Int):StudentEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(student:StudentEntity)

    @Update
    fun update(student: StudentEntity)

    @Delete
    fun delete(student: StudentEntity)
}