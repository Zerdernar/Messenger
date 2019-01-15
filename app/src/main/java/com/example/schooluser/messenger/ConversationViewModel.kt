package com.example.schooluser.messenger

import android.arch.lifecycle.MutableLiveData

class ConversationViewModel : DispatchViewModel() {
    companion object {
        const val PROFILE = "profile"
        const val CONVERSATION = "conversation"
        const val MENU = "menu"
        const val IMAGE = "image"
    }

    val window = MutableLiveData<String>()

    val store = MutableLiveData<Store>()

    val isOpenMenu = MutableLiveData<Boolean>()

    var message = MutableLiveData<String>()

    override suspend fun reduce(action: Action) {
        super.reduce(action)

        when (action) {
            is MainActions.OpenProfile -> {
                window.value = PROFILE
            }

            is MainActions.OpenConversation -> window.value = CONVERSATION

            is MainActions.OpenConversationNew -> {
                window.value = CONVERSATION
            }

            is MainActions.OpenMenu -> window.value = MENU

            is MainActions.OpenImage -> window.value = IMAGE

            is MainActions.OpenProfileBack -> window.value = PROFILE

            is MainActions.OpenConversationBack -> {
                window.value = CONVERSATION
            }
        }
    }
}


