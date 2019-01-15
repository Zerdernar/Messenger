package com.example.schooluser.messenger

import android.arch.lifecycle.LiveData
import timber.log.Timber

object ConversationRepository {
    fun getConversation(conversationId: String): DBConversation? {
        return AppDatabase.INSTANCE.conversations().getConversation(conversationId)
    }

    fun insertConversation(
        listItem: List<MessageData.Items>,
        listProfile: List<MessageData.Profiles>
//        listGroups: List<MessageData.Groups>
    ) {
        val listOfConversations = listItem.map { items ->
            DBConversation().also { db ->
                db.lastMessage = items.last_message.text
                when (items.conversation.peer.type) {
                    "user" -> {
                        db.id = items.conversation.peer.id
                        db.lastMessage =
                                if (((items.last_message.text).length) > 20) {
                                    "${items.last_message.text.take(17)}..."
                                } else {
                                    items.last_message.text
                                }
                        val userInfo = listProfile.first { it.id == items.conversation.peer.id }
                        db.lastMessageData = items.last_message.date
                        db.firstName = userInfo.first_name
                        db.lastName = userInfo.last_name
                        db.online = userInfo.online
                        db.photo = userInfo.photo_50
                        db.photoFull = userInfo.photo_100
                    }
                    "chat" -> {
                        db.id = items.conversation.peer.id
                        db.lastMessage =
                                if (((items.last_message.text).length) > 20) {
                                    "${items.last_message.text.take(17)}..."
                                } else {
                                    items.last_message.text
                                }
                        db.lastMessageData = items.last_message.date
                        db.lastName = items.conversation.chat_settings.title
                        db.firstName = ""
                        db.photo = items.conversation.chat_settings.photo?.photo_50 ?: ""
                        db.photoFull = items.conversation.chat_settings.photo?.photo_50 ?: ""
                    }
//                    "group" -> {
//                        db.id = items.conversation.peer.id
//                        db.lastMessage =
//                                if (((items.last_message.text).length) > 20) {
//                                    "${items.last_message.text.take(17)}..."
//                                } else {
//                                    items.last_message.text
//                                }
//                        val groupInfo = listGroups.first { it.id == items.conversation.peer.id }
//                        db.lastName = groupInfo.name ?: ""
//                        db.firstName = ""
//                        db.photo = groupInfo.photo_50 ?: ""
//                        db.photoFull = groupInfo.photo_100 ?: ""
//                    }
                }
            }
        }
        try {
            AppDatabase.INSTANCE.conversations().insertConversations(listOfConversations)
            Timber.e("AppDatabase.INSTANCE ${listOfConversations}")
        } catch (t: Throwable) {
            Timber.e("Timber insertConversation - ${t.localizedMessage}")
        }
    }

    fun getAllConversation(): LiveData<List<DBConversation>> {
        return AppDatabase.INSTANCE.conversations().getAllConversation()
    }
}