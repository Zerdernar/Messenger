package com.example.schooluser.messenger

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import android.arch.lifecycle.Observer
import io.reactivex.Flowable
import io.reactivex.Observable

fun <T> LiveData<T>.flow(lifecycleOwner: LifecycleOwner): Flowable<T?> =
    Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))

fun <T> LiveData<T>.observable(lifecycleOwner: LifecycleOwner): Observable<T?> =
    Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this))

fun <T> LiveData<T>.observableNotNull(lifecycleOwner: LifecycleOwner): Observable<T> =
    Observable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)).filter { it != null }

fun <T> LiveData<T>.flowNotNull(lifecycleOwner: LifecycleOwner): Flowable<T> =
    Flowable.fromPublisher(LiveDataReactiveStreams.toPublisher(lifecycleOwner, this)).filter { it != null }

fun <T> LiveData<T>.observe(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
    observe(lifecycleOwner, Observer { observer(it) })

fun <T> LiveData<T>.observeNotNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
    observe(lifecycleOwner, Observer { it?.let { observer(it) } })
