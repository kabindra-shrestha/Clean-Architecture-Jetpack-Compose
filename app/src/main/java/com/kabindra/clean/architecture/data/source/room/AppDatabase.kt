package com.kabindra.clean.architecture.data.source.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kabindra.clean.architecture.data.model.ApiTokenDTO
import com.kabindra.clean.architecture.data.model.UserDTO
import com.kabindra.clean.architecture.data.source.room.converter.FirebaseTopicsConverters
import com.kabindra.clean.architecture.data.source.room.dao.ApiTokenDao
import com.kabindra.clean.architecture.data.source.room.dao.UserDao

@Database(
    entities = [ApiTokenDTO::class, UserDTO::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(
    FirebaseTopicsConverters::class
)

abstract class AppDatabase : RoomDatabase() {
    abstract val apiTokenDao: ApiTokenDao
    abstract val userDao: UserDao
}
