package com.example.schooluser.messenger

interface Action {

    class Error(val error: Throwable) : Action

}