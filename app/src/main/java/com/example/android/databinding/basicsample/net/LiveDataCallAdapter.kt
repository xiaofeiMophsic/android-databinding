package com.example.android.databinding.basicsample.net

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  作者：xiaofei
 *  日期：2019/10/16
 */
@Suppress("UNCHECKED_CAST")
class LiveDataCallAdapter<T>(private val responseType: Type): CallAdapter<T, LiveData<T>>{
    override fun adapt(call: Call<T>): LiveData<T> {
        return object : LiveData<T>() {
            private val start = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (start.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<T>{
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val value = APIResponse<T>(null, -1, t.message?:"") as T
                            postValue(value)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(response.body())
                        }
                    })
                }
            }
        }
    }

    override fun responseType(): Type = responseType

}