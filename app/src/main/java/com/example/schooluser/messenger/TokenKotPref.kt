package com.example.schooluser.messenger

import com.chibatching.kotpref.KotprefModel

// объект kotpref'a
object TokenKotPref : KotprefModel() {
    // создадим переменную пустой(чтобы ничег оне записывать изначально)
    var token by nullableStringPref()
    var dialogId by nullableStringPref()
    var profileImage by nullableStringPref()
    var profileFirstName by nullableStringPref()
    var profileLastName by nullableStringPref()
}