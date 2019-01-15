package com.example.schooluser.messenger

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface ConversationDao{
    @Query("SELECT * FROM DBConversation WHERE id = :conversationId")
    fun getConversation (conversationId : String): DBConversation?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConversations(listConversation: List<DBConversation>)

    @Query("SELECT * FROM DBConversation ORDER BY lastMessageData DESC")
    fun getAllConversation():LiveData<List<DBConversation>>
}