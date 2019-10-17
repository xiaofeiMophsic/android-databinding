package com.example.android.databinding.basicsample.net

import androidx.lifecycle.LiveData
import com.example.android.databinding.basicsample.BuildConfig
import com.example.android.databinding.basicsample.model.Article
import com.example.android.databinding.basicsample.model.Banner
import com.example.android.databinding.basicsample.model.Page
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

/**
 *  作者：xiaofei
 *  日期：2019/10/16
 */
interface WanAPI {
    companion object {
        fun get() : WanAPI{
            val clientBuilder = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
            if(BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
            }
            return Retrofit.Builder()
                    .baseUrl("https://www.wanandroid.com/")
                    .client(clientBuilder.build())
                    .addCallAdapterFactory(LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(WanAPI::class.java)
        }
    }

    /**
     * https://www.wanandroid.com/banner/json
     */
    @GET("banner/json")
    fun bannerList(): LiveData<APIResponse<List<Banner>>>

    /**
     * https://www.wanandroid.com/article/list/0/json
     */
    @GET("article/list/{page}/json")
    fun articleList(@Path("page") page: Int): LiveData<APIResponse<Page<Article>>>
}