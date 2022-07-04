package com.karyadika.githubuser.utils

interface ViewStateCallback<T> {
    fun onSuccess(data: T)
    fun onLoading()
    fun onFailed(message: String?)
}