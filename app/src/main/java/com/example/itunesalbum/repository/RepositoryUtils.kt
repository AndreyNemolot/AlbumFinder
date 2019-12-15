package com.example.itunesalbum.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

//Coroutines wrapper for retrofit answer

suspend fun <T> Call<T>.await(): T = suspendCoroutine {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) {
            it.resumeWithException(t)
        }

        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    it.resume(body)
                    return
                }
            }
            it.resumeWithException(Exception("Response code is not success"))
        }
    })
}