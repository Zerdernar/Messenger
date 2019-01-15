package com.example.schooluser.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import com.chibatching.kotpref.livedata.asLiveData
import com.example.schooluser.messenger.ConversationViewModel.Companion.CONVERSATION
import com.example.schooluser.messenger.ConversationViewModel.Companion.IMAGE
import com.example.schooluser.messenger.ConversationViewModel.Companion.MENU
import com.example.schooluser.messenger.ConversationViewModel.Companion.PROFILE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private val conversationViewModel by viewModel<ConversationViewModel>()

    //для функции авторендера левого меню которое внизу
    private var drawerListener: RightMenuListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (TokenKotPref.token.isNullOrEmpty()) {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            Timber.d("Token Empty")
        } else {
            //создаем пустой диалог лист в сторе
            conversationViewModel.store.value = Store(listOfDialog = listOf())
            // функция базового запроса диалогов
            getDialogsApi(TokenKotPref.token ?: "")
            // функция базового запроса окна диалога
            getMessageApi(TokenKotPref.dialogId ?: "76219157", TokenKotPref.token ?: "")
            // функция автозапроса диалогов
            TokenKotPref.asLiveData(TokenKotPref::dialogId).observe(this) {
                getMessageApi(TokenKotPref.dialogId ?: "76219157", TokenKotPref.token ?: "")
            }

            // для функции авторендера
            drawerListener = RightMenuListener()

            // TODO поменять deprecated method setDrawerListener
            // для функции авторендера
            main_drawer.setDrawerListener(drawerListener)

            // вставляем фрагмент
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(R.id.main_frame, ConversationFragment())
                .commit()

            // смотрит за месенджем и вызывает api отправки сообщений и запрашивает историю диалогов
            conversationViewModel.message.observe(this) {
                messageSend(
                    TokenKotPref.dialogId ?: "76219157",
                    TokenKotPref.token ?: "",
                    conversationViewModel.message.value ?: ""
                )
            }
            // перезапись из BD в Store
            ConversationRepository.getAllConversation().observe(this) { item ->
                val list = ConversationRepository.getAllConversation()
                Timber.d("Timber  ConversationRepository.getAllConversation()  - ${item?.size}")
                val storeDialogs = item?.map {

                    Store.DialogList(
                        id = it.id,
                        lastMessage = it.lastMessage,
                        lastName = it.lastName,
                        firstName = it.firstName,
                        image = it.photo,
                        imageFull = it.photoFull,
                        status = it.online
                    )
                }
                conversationViewModel.store.value = conversationViewModel.store.value?.copy(
                    listOfDialog = storeDialogs ?: emptyList()
                )
            }

            // наблюдаем за изменением диалог id и закрываем левое меню
            TokenKotPref.asLiveData(TokenKotPref::dialogId).observe(this) {
                main_drawer.closeDrawers()
            }

            // наблюдает (смена фрагментов)
            conversationViewModel.window.observe(this) {
                // если
                when (it) {
                    PROFILE -> {
                        // меняем фаргмент
                        fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, ProfileFragment())
                            .commit()
                        // закрываем левое меню
                        main_drawer.closeDrawers()

                    }
                    CONVERSATION -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, ConversationFragment())
                            .commit()
                        main_drawer.closeDrawers()
                    }

                    MENU -> {
                        main_drawer.openDrawer(GravityCompat.START)
                    }
                    IMAGE -> {
                        fragmentManager.beginTransaction()
                            .replace(R.id.main_frame, ImageFragment())
                            .commit()
                    }
                }

            }
        }
    }
    // функция получения инфо профиля
//    private fun getUsers(id: String, token: String, message: String) {
//        GlobalScope.launch(Dispatchers.Default) {
//            Client().vkApi.getUsers(
//                version = "5.92",
//                user_id = id,
////                fields =
//                token = token
//            ).asyncAwait()
//            getMessageApi(id, token)
//            getDialogsApi(token)
//        }
//    }

    //функция отправки сообщения api
    private fun messageSend(id: String, token: String, message: String) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                Client().vkApi.getMessSend(
                    version = "5.92",
                    user_id = id,
                    random_id = Random.nextInt().toString(),
                    message = message,
                    token = token
                ).asyncAwait()
                getMessageApi(id, token)
                getDialogsApi(token)
            } catch (t: Throwable) {
                Timber.e("ERROR MESSAGE SEND ${t.toString()}")
            }
        }

    }

    // функция вызова сообщений api
    private fun getMessageApi(id: String, token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                Client().vkApi.getMessList(
                    version = "5.92",
                    count = "20",
                    userId = id,
                    extended = "1",
                    token = token
                ).asyncAwait()
                    .let { data ->
                        val list = mutableListOf<Store.Message>()
                        data.response.items.forEach { items ->
                            val messObject = Store.Message()
                            messObject.messText = items.text
                            messObject.id = items.from_id
                            val userInfo = data.response.profiles.first { it.id == items.from_id }
                            messObject.lastName = userInfo.last_name
                            messObject.firstName = userInfo.first_name
                            messObject.image = userInfo.photo_50
                            messObject.imageFull = userInfo.photo_100
                            list.add(messObject)
                        }
                        withContext(Dispatchers.Main) {
                            conversationViewModel.store.value = conversationViewModel.store.value?.copy(
                                listOfMessage = list
                            )
                        }
                    }
            } catch (t: Throwable) {
                Timber.e("ERROR GET MESSAGE ${t.toString()}")
            }
        }

    }


    // TODO все что подсвечено студией исправить
    // функция запроса списка диалогов api
    private fun getDialogsApi(token: String) {
        GlobalScope.launch(Dispatchers.Default) {
            try {
                Client().vkApi.getDialogList(
                    count = "20",
                    extended = "1",
                    token = token,
                    version = "5.92"
                )
                    .asyncAwait()
                    .let {
                        ConversationRepository.insertConversation(
                            it.response.items,
                            it.response.profiles
//                            it.response.groups?: emptyList()
                        )
                    }
            } catch (t: Throwable) {
                Timber.e("ERROR GET DIALOGS ${t.toString()}")
            }
        }

    }


    //фнукция нажатия на кнопку назад
    override fun onBackPressed() {
        // TODO WHEN ПОЖАЛУЙСТА ИСПОЛЬЗУЙ
        //если открыта imagefragment то переоткрывам profilefragment
        if (conversationViewModel.window.value == IMAGE) {
            conversationViewModel.window.value = PROFILE
            //а если открыт profilefragment то нужно переоткрыть conversationfragment
        } else if (conversationViewModel.window.value == PROFILE) {
            conversationViewModel.window.value = CONVERSATION
            //а если открыт conversationfragment либо leftmenu то
        } else if (conversationViewModel.window.value == CONVERSATION || conversationViewModel.window.value == MENU) {
            //если leftmenu открыто,
            if (main_drawer.isDrawerOpen(GravityCompat.START)) {
                // то закрываем
                main_drawer.closeDrawers()
                //если нет, то открываем
            } else {
                main_drawer.openDrawer(GravityCompat.START)
            }
        }
    }

    //функция авторендера левого меню
    inner class RightMenuListener : DrawerLayout.DrawerListener {

        override fun onDrawerSlide(p0: View, p1: Float) {
        }

        override fun onDrawerStateChanged(p0: Int) {
        }

        //следит заткрыто ли левое меню
        override fun onDrawerClosed(view: View) {
            conversationViewModel.isOpenMenu.value = false
        }

        //следит открыто ли левое меню
        override fun onDrawerOpened(p0: View) {
            // авто-перезапрос api списк диал в функции авто-перерендера
            getDialogsApi(TokenKotPref.token ?: "")
            conversationViewModel.isOpenMenu.value = true
        }
    }

}


