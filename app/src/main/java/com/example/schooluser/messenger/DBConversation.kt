package com.example.schooluser.messenger

import android.arch.persistence.room.Entity

@Entity(
    primaryKeys = ["id"]
)
open class DBConversation{
    var conversationId: String = ""
    var id: Int = 0
    var lastMessage = ""
    var lastMessageData = ""
    var firstName: String = ""
    var lastName: String = ""
    var photo: String = ""
    var photoFull: String = ""
    var online: Int = 0
}