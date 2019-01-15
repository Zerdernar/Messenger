package com.example.schooluser.messenger

import android.graphics.Color
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.generic.RoundingParams
import com.facebook.litho.*
import com.facebook.litho.annotations.*
import com.facebook.litho.fresco.FrescoImage
import com.facebook.litho.widget.Text
import com.facebook.yoga.YogaEdge
import com.facebook.yoga.YogaJustify
import io.reactivex.subjects.PublishSubject


@LayoutSpec
object ListItemSpec {

    @OnCreateLayout
    fun onCreateLayout(
        c: ComponentContext,
        @Prop id: Int,
        @Prop lastMessage: String,
        @Prop lastName: String,
        @Prop firstName: String,
        @Prop image: String,
        @Prop imageFull: String,
        @Prop status: Int
    ): Component {
        //создание колонны
        return Row.create(c)
            //Паддинг сов сех сторон(YogaEdge.ALL)
            .paddingDip(YogaEdge.ALL, 5f)
            .backgroundColor(Color.WHITE)
            .justifyContent(YogaJustify.FLEX_START)
            .clickHandler(
                ListItem.onClick(c, id, image,imageFull,firstName,lastName)
            )
            .child(
                //вставляем картинку в формате фреско(потому что умеет читать не изображения а ссылки
                FrescoImage.create(c)
                    //марджин с права
                    .marginDip(YogaEdge.RIGHT, 10f)
                    //высота 30дп
                    .widthDip(50f)
                    //ширина 30дп
                    .heightDip(50f)
                    //параметр который округляет изображение
                    .roundingParams(RoundingParams.asCircle())
                    //вешаем ссылку на клик(клик описан в низу через busIvent
                    .controller(
                        //путь до картинки
                        Fresco.newDraweeControllerBuilder()
                            .setUri(image)
                            .build()
                    )
            )
            .child(
                Column.create(c)
                    .child(
                        //текстВюшка для Имени и фамилии
                        Text.create(c)
                            .text("$firstName $lastName")
                            .textColor(Color.GRAY)
                            .textSizeSp(20f)
//                    .clickHandler(ListItem.onClickName(c, index, firstName, lastName, faceImage))
                    )
                    .child(
                        //текстВюшка для Имени и фамилии
                        Text.create(c)
                            .text("$lastMessage")
                            .textColor(Color.GRAY)
                            .textSizeSp(20f)
//                    .clickHandler(ListItem.onClickName(c, index, firstName, lastName, faceImage))
                    )
            ).build()

    }

    //    обрабатывает нажатие
    @OnEvent(ClickEvent::class)
    fun onClick(
        c: ComponentContext,
        @Param id: Int,
        @Param image: String,
        @Param imageFull: String,
        @Param lastName: String,
        @Param firstName: String,
        @Prop busEvent: PublishSubject<Any>
    ) {
//    отправляет событие
        busEvent.onNext(Events.Click(id,image,imageFull,lastName,firstName))
    }
}