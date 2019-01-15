package com.example.schooluser.messenger

data class MessageData(
    val response: Response
) {
    data class Response(
        val items: List<Items>,
        val unread_count: String,
        val profiles: List<Profiles>,
        val groups: List<Groups>?
    )

    data class Items(
        val conversation: Conversation,
        val last_message: LastMessage
    )

    data class Conversation(
        val peer: Peer,
        val chat_settings: ChatSettings
    )

    data class Peer(
        val id: Int = 0,
        val type: String
    )

    data class ChatSettings(
        val title: String,
        val photo: Photo?
    )

    data class Photo(
        val photo_50: String?
    )

    data class LastMessage(
        val date: String,
        val text: String
    )

    data class Profiles(
        val id: Int,
        val first_name: String,
        val last_name: String,
        val photo_50: String,
        val photo_100: String,
        val online: Int
    )

    data class Groups(
        val id: Int?,
        val name: String?,
        val photo_50: String?,
        val photo_100: String?
    )
}


