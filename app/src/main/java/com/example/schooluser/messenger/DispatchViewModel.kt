package com.example.schooluser.messenger

import android.arch.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class DispatchViewModel : ViewModel() {
    val dispatcher: PublishSubject<Action> = PublishSubject.create()

    protected open suspend fun reduce(action: Action) {}

    private suspend fun dispatch(action: Action) {

        try {
            action.let {
                dispatcher.onNext(it)
                reduce(it)
            }
        } catch (t: Throwable) {
            Action.Error(t).let {
                dispatcher.onNext(it)
                reduce(it)
            }
        }
    }

    suspend fun dispatchAwait(action: suspend () -> Action) {
        return withContext(Dispatchers.Main) {
            dispatch(action())
        }
    }

    fun dispatch(action: suspend () -> Action) {
        GlobalScope.launch(Dispatchers.Main) {
            dispatchAwait(action)
        }
    }

    inline fun <reified T : Any> listen(): Observable<T> = dispatcher
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .ofType(T::class.java)

}