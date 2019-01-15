package com.example.schooluser.messenger

interface MainActions:Action {
    //создали класс в интерфейс
    class OpenProfile(val id:String):MainActions
    class OpenConversation:MainActions
    class OpenMenu:MainActions
    class OpenImage:MainActions
    class OpenProfileBack:MainActions
    class OpenConversationBack(val id:String):MainActions
    class OpenConversationNew(val id:String):MainActions
}