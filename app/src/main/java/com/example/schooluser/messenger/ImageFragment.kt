package com.example.schooluser.messenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_image.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

class ImageFragment : Fragment() {

    private val conversationViewModel by sharedViewModel<ConversationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onResume() {
        super.onResume()

        btn_back_image.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenProfileBack()
                }

            }
    }


}


