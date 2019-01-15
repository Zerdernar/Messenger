package com.example.schooluser.messenger

data class GetUsersData(
    val responce: Responce
){
    data class Responce(
        val first_name: String,
        val last_name: String,
        val photo_200: String,
        val photo_max: String,
        val online: String,
        val status: String
    )
}