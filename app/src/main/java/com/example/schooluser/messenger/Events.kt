package com.example.schooluser.messenger

interface Events {
    class Click(
        val id: Int,
        val image: String,
        val imageFull: String,
        val lastName: String,
        val firstName: String
    )

    class ClickFace(
        val id: String,
        val image: String,
        val lastName: String,
        val firstName: String
    )
}