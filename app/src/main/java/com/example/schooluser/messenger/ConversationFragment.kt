package com.example.schooluser.messenger

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chibatching.kotpref.livedata.asLiveData
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.ListRecyclerConfiguration
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_conversation.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ConversationFragment : Fragment() {

    private val conversationViewModel by sharedViewModel<ConversationViewModel>()

    private val busEvent = PublishSubject.create<Any>()

    // TODO пробелы убрать лишние, проверить все переменные на правописание и подчеркнутые поменять приватность
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        return inflater.inflate(R.layout.fragment_conversation, container, false)
    }

    //рендер списка месенджей
    private fun renderMesseges() {
        //переменная для того что бы запихнуть конфигурация
        val listResyclerConfiguration = ListRecyclerConfiguration.create()
            .orientation(LinearLayoutManager.VERTICAL)
            .reverseLayout(true)
            .build()
        //переменная
        val sectionContext = SectionContext(name_group_bar.context)
        // закоментить
        val component = RecyclerCollectionComponent.create(sectionContext)
            .disablePTR(true)
            .recyclerConfiguration(listResyclerConfiguration)
            .section(
                ListMessengesSection.create(sectionContext)
                    .listOfMessage(conversationViewModel.store.value?.listOfMessage ?: listOf())
                    .busEvent(busEvent)
                    .build()
            )
            .build()
        message_box.setComponent(component)
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    override fun onResume() {
        super.onResume()

        // стоковая установка верхнего бара(при запуске)
        name_bar.text = TokenKotPref.profileLastName + " " + TokenKotPref.profileFirstName
        group_face.setImageURI(TokenKotPref.profileImage)

        // функция авторендера мессенджей
        conversationViewModel.store.observe(this) {
            renderMesseges()
        }
        // временный выход из аккаунта(пока чтопере получает токен)
        settings.setOnClickListener {
            TokenKotPref.token = null
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }

        // не работает
        // функция замены кнопки микрофона на кнопку enter
        text_message.textChanges()
            .liveData(this) {
                if (it == "") {
                    btn_enter.setImageResource(R.drawable.mic_none)
                } else {
                    btn_enter.setImageResource(R.drawable.enter)
                }
            }

        // вызов левого меню(через кнопку)
        btn_menu_klick_left.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenMenu()
                }
            }
        // нажатие бас ивент привязывается по типу к кастом эвенту
        busEvent.ofType<Events.Click>()
            //Незваисимо от жизненного цикла
            .autoDisposable(scope())
            .subscribe { event ->
                // записываем в store  данные аккаунта на который нажали
                TokenKotPref.dialogId = event.id.toString()
                TokenKotPref.profileFirstName = event.firstName
                TokenKotPref.profileLastName = event.lastName
                TokenKotPref.profileImage = event.imageFull
                conversationViewModel.dispatch {
                    MainActions.OpenProfile("${TokenKotPref.dialogId}")
                }
            }
        // переделать на store!!
        // если измениться id в котпрефе то поменяет следующие строки
        TokenKotPref.asLiveData(TokenKotPref::dialogId).observe(this) {
            name_bar.text = "${TokenKotPref.profileLastName} ${TokenKotPref.profileFirstName}"
            group_face.setImageURI(TokenKotPref.profileImage)
        }
        //клик на название группы
        name_bar.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenProfile("${TokenKotPref.dialogId}")
                }
            }
        // клик на картинку группы
        group_face.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenProfile("${TokenKotPref.dialogId}")
                }
            }
        //обработка клика на enter
        btn_enter.clicks()
            .autoDisposable(scope())
            .subscribe {
                val message = text_message.text.toString()
                text_message.text.clear()
                if (message != "") {
                    conversationViewModel.message.value = message
                }
                renderMesseges()
            }
//          рендерит сообщение
        conversationViewModel.store.observe(this) {
            renderMesseges()
        }
    }
}