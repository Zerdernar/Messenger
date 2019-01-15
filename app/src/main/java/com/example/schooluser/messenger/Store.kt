package com.example.schooluser.messenger

data class Store(

    var profileId: String = "",

    val listOfMessage: List<Store.Message> = emptyList(),

    val listOfDialog: List<Store.DialogList> = emptyList()
) {
    data class Mess(
        var mess: String = ""
    )

    data class Message(
        var id: Int = 0,
        var messText: String = "",
        var lastName: String = "",
        var firstName: String = "",
        var image: String = "",
        var imageFull: String = ""
    )

    data class DialogList(
        var id: Int = 0,
        var lastMessage: String = "",
        var lastName: String = "",
        var firstName: String = "",
        var image: String = "",
        var imageFull: String = "",
        var status: Int = 0
    )
}
