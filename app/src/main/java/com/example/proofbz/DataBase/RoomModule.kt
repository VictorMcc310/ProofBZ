package com.example.proofbz.DataBase

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val  STUDENT_DATA_BASE = "student_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context:Context) =
        Room.databaseBuilder(context, StudentDataBase::class.java, STUDENT_DATA_BASE).allowMainThreadQueries().build()

    @Singleton
    @Provides
    fun provideStudentDao(db:StudentDataBase) =db.getStudentDao()


}