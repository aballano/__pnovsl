package com.aballano.cabishop.common

import androidx.lifecycle.MutableLiveData

val <T> T.exhaustive: T
    get() = this

fun CharSequence.intOrZero(): Int =
    toString().toIntOrNull() ?: 0

fun <T> MutableLiveData<T>.postIfDistinct(it: T) {
    if (value != it) postValue(it)
}