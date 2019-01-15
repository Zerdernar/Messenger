package com.example.schooluser.messenger

open class NoInternetException : Throwable()

open class ResponseCommand : Throwable()

class NeedCallStartCommand(val url: String) : ResponseCommand()

class NeedAttachEmailCommand : ResponseCommand()

class ForceUpdateCommand(val url: String) : ResponseCommand()

open class ResponseError : Throwable()

open class HttpResponseError(val errorCode: Int) : ResponseError()

open class EmptyResponseError : ResponseError()

open class UnauthorizedError : ResponseError()

open class CommonResponseError : ResponseError() {

    val errorCode: Int = 0

    val errorMessage: String? = null

    override fun getLocalizedMessage(): String = errorMessage ?: "Unknown error code $errorCode"
}