package com.example.schooluser.messenger

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import splitties.init.appCtx

@Database(
    entities = [
        DBConversation::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        val INSTANCE by lazy { Room.databaseBuilder(appCtx, AppDatabase::class.java,"Database").build() }

        fun runInTransaction(run: () -> Unit) {
            INSTANCE.runInTransaction(run)
        }
    }
    abstract fun conversations():ConversationDao
}