package com.example.schooluser.messenger

data class MessData(
    val response: Response
) {
    data class Response(
        val items: List<Items>,
        val profiles: List<Profiles>
    )

    data class Items(
        val text: String,
        val from_id: Int
    )

    data class Profiles(
        val id: Int,
        val first_name: String,
        val last_name: String,
        val photo_50: String,
        val photo_100: String
    )
}