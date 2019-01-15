package com.example.schooluser.messenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.widget.RecyclerCollectionComponent
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.rxkotlin.ofType
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.fragment_left_menu.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class LeftMenuFragment : Fragment() {

    private val conversationViewModel by sharedViewModel<ConversationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_left_menu, container, false)
    }

    //создали нужынй Observable для опубликования событий при нажатии на кнопки в лита
    private val busEvent = PublishSubject.create<Any>()

    //ну типо функция отображения списка "людей"
    private fun render(listOfMessage: List<Store.DialogList>?) {

        val sectionContext = SectionContext(left_dialog.context)

        val component = RecyclerCollectionComponent.create(sectionContext)
            .disablePTR(true)
            .section(
                ListSection.create(sectionContext)
                    .busEvent(busEvent)
                    .listOfMessage(listOfMessage)
                    .build()
            )
            .build()

        left_dialog.setComponent(component)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //отрисовка
        conversationViewModel.store.observe(this) {
            render(it?.listOfDialog)
        }
        //смотрит за переменной и если тру то запускает рендер
        conversationViewModel.isOpenMenu.observe(this) { value ->
            if (value == true) {
                render(conversationViewModel.store.value?.listOfDialog ?: emptyList())
            }
        }

    }

    override fun onResume() {
        super.onResume()

        //()нажатие бас ивент привязывается по типу к кастом эвенту
        busEvent.ofType<Events.Click>()
            //Незваисимо от жизненного цикла
            .autoDisposable(scope())
            .subscribe { event ->
                TokenKotPref.dialogId = event.id.toString()
                TokenKotPref.profileFirstName = event.firstName
                TokenKotPref.profileLastName = event.lastName
                TokenKotPref.profileImage = event.image
            }
    }
}



