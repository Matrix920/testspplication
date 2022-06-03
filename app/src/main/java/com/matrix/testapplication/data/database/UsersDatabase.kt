package com.matrix.testapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.matrix.testapplication.data.model.DbUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [DbUser::class], version = 1)
abstract class UsersDatabase: RoomDatabase() {

    abstract val usersDao: UsersDao

    companion object {
        private lateinit var INSTANCE: UsersDatabase

        fun getDatabase(context: Context, scope: CoroutineScope): UsersDatabase {
            synchronized(UsersDatabase::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UsersDatabase::class.java,
                        "users"
                    )
                        .addCallback(UsersDatabaseCallback(scope))
                        .build()
                }
            }
            return INSTANCE
        }
    }

    private class UsersDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }
}