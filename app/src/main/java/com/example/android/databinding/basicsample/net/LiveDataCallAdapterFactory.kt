package com.example.android.databinding.basicsample.net

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 *  作者：xiaofei
 *  日期：2019/10/16
 */
class LiveDataCallAdapterFactory: CallAdapter.Factory(){
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if(getRawType(returnType) != LiveData::class.java)
            return null
        val observable = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawType = getRawType(observable)
        if (rawType != APIResponse::class.java) {
            throw IllegalArgumentException("Type must be APIResponse")
        }
        if (observable !is ParameterizedType) {
            throw IllegalArgumentException("Resource must be parameterized")
        }
        return LiveDataCallAdapter<Any>(observable)
    }

}