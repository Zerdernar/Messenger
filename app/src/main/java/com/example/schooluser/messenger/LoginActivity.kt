package com.example.schooluser.messenger

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_login.*
import timber.log.Timber


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // TODO прочитать про тройные кавычки и реорганизовать эту строку так, чтобы она была легкочитаемая
        val url =
            "https://oauth.vk.com/authorize?client_id=6786682&display=mobile&redirect_uri=https://oauth.vk.com/blank.html&scope=messages,friends,offline&response_type=token&v=5.52"

        webv_view.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url.toString().startsWith("https://oauth.vk.com/blank.html#")) {

                    val regex = "(?:access_token)\\=([\\S\\s]*?)\\&".toRegex()

                    val regexSecond = "access_token=([\\S\\s]*?)&".toRegex()

                    val token = regex.find(url.toString())?.value

                    val tokenSecond = regexSecond.find(token.toString())?.groups?.last()?.value.toString()
                    // записать
                    TokenKotPref.token = tokenSecond

//                    if (TokenKotPref.token.isNullOrEmpty()) {
//                        Timber.d("Token Empty!")
//                    } else {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
//                    }
                }
            }
        }
        webv_view.loadUrl(url)
    }


}

