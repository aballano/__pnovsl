package com.aballano.cabishop.common

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class DisposablePresenter : ViewModel() {

    private val compositeDisposable by lazy(::CompositeDisposable)

    protected fun Disposable.trackDisposable() {
        this@DisposablePresenter.compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}