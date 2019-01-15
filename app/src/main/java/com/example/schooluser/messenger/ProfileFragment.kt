package com.example.schooluser.messenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.uber.autodispose.android.lifecycle.scope
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.concurrent.TimeUnit

class ProfileFragment : Fragment() {

    private val conversationViewModel by sharedViewModel<ConversationViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onResume() {
        super.onResume()

        back_klick_profile.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenConversation()
                }
            }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_name.text =
                ("${TokenKotPref.profileFirstName} ${TokenKotPref.profileLastName}")
        profile_face.setImageURI(TokenKotPref.profileImage)

        profile_face.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenImage()

                }
            }

        btn_new_message.clicks().debounce(400L, TimeUnit.MILLISECONDS)
            .autoDisposable(scope())
            .subscribe {
                conversationViewModel.dispatch {
                    MainActions.OpenConversationNew("$id")

                }
            }
    }
}