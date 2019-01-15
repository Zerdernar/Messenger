package com.example.schooluser.messenger

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface VkApi {
    @GET("method/messages.getConversations")
    fun getDialogList(
        @Query("v") version: String,
        @Query("count") count: String,
        @Query("extended") extended: String,
        @Query("access_token") token: String
    ): Single<MessageData>

    @GET("method/messages.getHistory")
    fun getMessList(
        @Query("v") version: String,
        @Query("count") count: String,
        @Query("peer_id") userId: String,
        @Query("extended") extended: String,
        @Query("access_token") token: String
    ): Single<MessData>

    @GET("method/messages.send")
    fun getMessSend(
        @Query("v") version: String,
        @Query("peer_id") user_id: String,
        @Query("random_id") random_id: String,
        @Query("message") message: String,
        @Query("access_token") token: String
    ): Single<SandData>

    @GET("method/users.get")
    fun getUsers(
        @Query("v") version: String,
        @Query("user_ids") user_id: String,
        @Query("fields") random_id: String,
        @Query("access_token") token: String
    ): Single<GetUsersData>
}