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
object ListMessegesSpec {


    //функция которая позволяет нам сверстать
    //наш экран(Layout или компонент) который будет отображаться в LitheView
    @OnCreateLayout
    fun onCreateLayout(
        c: ComponentContext,
        @Prop id: Int,
        @Prop text: String,
        @Prop firstName: String,
        @Prop lastName: String,
        @Prop image: String,
        @Prop imageFull: String,
        @Prop busEvent: PublishSubject<Any>
    ): Component {
        //создание колонны
        return Row.create(c)
            //паддинг сов сех сторон(YogaEdge.ALL)
            .paddingDip(YogaEdge.ALL, 5f)
            .backgroundColor(Color.WHITE)
            .justifyContent(YogaJustify.FLEX_START)
            .child(
                //вставляем картинку в формате фреско(потому что умеет читать не изображения а ссылки
                FrescoImage.create(c)
                    //марджин с права
                    .marginDip(YogaEdge.RIGHT, 10f)
                    //высота 50дп
                    .widthDip(40f)
                    //ширина 50дп
                    .heightDip(40f)
                    //параметр который округляет изображение
                    .roundingParams(RoundingParams.asCircle())
                    //параметр который не дает уменьшать картинку
                    .flexShrink(0f)
                    .clickHandler(
                        ListItem.onClick(c, id, image, imageFull, firstName, lastName)
                    )
                    //путь до картинки
                    .controller(
                        Fresco.newDraweeControllerBuilder()
                            .setUri(image)
                            .build()
                    )

            )
            .child(
                Column.create(c)

                    .child(
                        //ТекстВюшка для Имени и фамилии собеседника
                        Text.create(c)
                            .text("${firstName} ${lastName}")
                            .textColor(Color.BLACK)
                            .textStyle(1)
                            .textSizeSp(20f)
                    )
                    .child(
                        //ТекстВюшка для текста сообщения
                        Text.create(c)
                            .text("${text}")
                            .textColor(Color.BLACK)
                            .textSizeSp(20f)
                    )
            )
            .build()
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
        busEvent.onNext(Events.Click(id, image, imageFull, lastName, firstName))

    }
}
