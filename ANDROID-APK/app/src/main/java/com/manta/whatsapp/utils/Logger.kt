package com.manta.whatsapp.utils

import timber.log.Timber

object Logger {
    fun d(msg: String) = Timber.d(msg)
    fun e(msg: String, t: Throwable? = null) = if (t != null) Timber.e(t, msg) else Timber.e(msg)
    fun i(msg: String) = Timber.i(msg)
    fun w(msg: String) = Timber.w(msg)
}