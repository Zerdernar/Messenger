package com.example.schooluser.messenger

import com.facebook.litho.annotations.Prop
import com.facebook.litho.sections.Children
import com.facebook.litho.sections.SectionContext
import com.facebook.litho.sections.annotations.GroupSectionSpec
import com.facebook.litho.sections.annotations.OnCreateChildren
import com.facebook.litho.sections.common.SingleComponentSection
import io.reactivex.subjects.PublishSubject

@GroupSectionSpec
object ListSectionSpec {
    //с помощью лэйаут спека создает список(для Layout
    //componentContext а для  SectionSpec SectionContext
    @OnCreateChildren
    fun onCreateChildren(
        c: SectionContext,
        @Prop busEvent: PublishSubject<Any>,
        @Prop listOfMessage: List<Store.DialogList>?
    ): Children {
        val builder = Children.create()
        //бежит и для элемента каждого списка создает спек
        listOfMessage?.forEach {
            builder.child(
                SingleComponentSection.create(c)
                    .component(
                        ListItem.create(c)
                            .id(it.id)
                            .lastMessage(it.lastMessage)
                            .lastName(it.lastName)
                            .firstName(it.firstName)
                            .image(it.image)
                            .imageFull(it.imageFull)
                            .status(it.status)
                            .busEvent(busEvent)
                            .build()
                    )
            )
        }
        return builder.build()
    }

}
