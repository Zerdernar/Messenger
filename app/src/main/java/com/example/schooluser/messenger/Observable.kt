package com.example.schooluser.messenger

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveDataReactiveStreams
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.*
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.rx2.await

fun <T> Observable<T>.dispatch(scope: ScopeProvider, model: DispatchViewModel, action: (T) -> Action): Disposable =
    autoDisposable(scope).subscribe {
        model.dispatch { action(it) }
    }

fun <T> Flowable<T>.dispatch(scope: ScopeProvider, model: DispatchViewModel, action: (T) -> Action): Disposable =
    autoDisposable(scope).subscribe {
        model.dispatch { action(it) }
    }

fun <T> Observable<T>.dispatch(lifecycleOwner: LifecycleOwner, model: DispatchViewModel, action: (T?) -> Action) =
    liveData().observe(lifecycleOwner) {
        model.dispatch { action(it) }
    }

fun <T> Flowable<T>.dispatch(lifecycleOwner: LifecycleOwner, model: DispatchViewModel, action: (T?) -> Action) =
    liveData().observe(lifecycleOwner) {
        model.dispatch { action(it) }
    }

fun <T> Single<T>.io(): Single<T> = subscribeOn(Schedulers.io())
fun <T> Maybe<T>.io(): Maybe<T> = subscribeOn(Schedulers.io())
fun <T> Flowable<T>.io(): Flowable<T> = subscribeOn(Schedulers.io())
fun <T> Observable<T>.io(): Observable<T> = subscribeOn(Schedulers.io())

fun <T> Single<T>.computation(): Single<T> = subscribeOn(Schedulers.computation())
fun <T> Maybe<T>.computation(): Maybe<T> = subscribeOn(Schedulers.computation())
fun <T> Flowable<T>.computation(): Flowable<T> = subscribeOn(Schedulers.computation())
fun <T> Observable<T>.computation(): Observable<T> = subscribeOn(Schedulers.computation())

fun <T> Single<T>.async(): Single<T> = this
    .subscribeOn(Schedulers.io())
    .retry(4)

suspend fun <T> Single<T>.asyncAwait(): T = this
    .async()
    .await()

fun <T> Observable<T>.liveData(strategy: BackpressureStrategy = BackpressureStrategy.BUFFER) =
    LiveDataReactiveStreams.fromPublisher<T>(this.toFlowable(strategy))

fun <T> Observable<T>.liveData(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
    liveData().observe(lifecycleOwner) { observer(it) }

fun <T> Observable<T>.liveDataNotNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
    liveData().observeNotNull(lifecycleOwner) { observer(it) }

fun <T> Flowable<T>.liveData() = LiveDataReactiveStreams.fromPublisher<T>(this)

fun <T> Flowable<T>.liveData(lifecycleOwner: LifecycleOwner, observer: (T?) -> Unit) =
    liveData().observe(lifecycleOwner) { observer(it) }

fun <T> Flowable<T>.liveDataNotNull(lifecycleOwner: LifecycleOwner, observer: (T) -> Unit) =
    liveData().observeNotNull(lifecycleOwner) { observer(it) }