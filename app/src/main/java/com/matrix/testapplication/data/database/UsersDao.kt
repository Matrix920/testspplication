package com.matrix.testapplication.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.matrix.testapplication.data.model.DbUser

@Dao
interface UsersDao {

    @Query("select * from user where id = :userId")
    fun getUser(userId: Int): LiveData<DbUser>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: DbUser)

    @Update
    suspend fun updateUser(user: DbUser)

    @Delete
    suspend fun deleteUser(user: DbUser)
}