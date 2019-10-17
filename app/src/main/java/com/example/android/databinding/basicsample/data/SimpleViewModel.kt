/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.databinding.basicsample.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.databinding.basicsample.model.Article
import com.example.android.databinding.basicsample.model.Banner
import com.example.android.databinding.basicsample.model.Page
import com.example.android.databinding.basicsample.net.APIResponse
import com.example.android.databinding.basicsample.net.WanAPI

/**
 * A simple VM for [com.example.android.databinding.basicsample.ui.PlainOldActivity].
 */
class SimpleViewModel : ViewModel() {

    private val _name = MutableLiveData("Liu")
    private val _lastName = MutableLiveData("xiaofei")
    private val _likes = MutableLiveData(0)

    private val _bannerLoading = MutableLiveData<Boolean>()
    private val _pageLoading = MutableLiveData<Boolean>()

    private val refershTrigger = MutableLiveData<Boolean>()
    private val api = WanAPI.get()
    private val bannerList: LiveData<APIResponse<List<Banner>>> = Transformations.switchMap(refershTrigger) {
        api.bannerList()
    }

    val banners:LiveData<List<Banner>> = Transformations.map(bannerList) {
        _bannerLoading.value = false
        it.data
    }

    val hasMore = MutableLiveData<Boolean>()

    private val page = MutableLiveData<Int>()

    private val articleList: LiveData<APIResponse<Page<Article>>> = Transformations.switchMap(page) {
        Log.d("ViewModel", "page changed -> $it")
        api.articleList(it)
    }
    val articles: LiveData<Page<Article>> = Transformations.map(articleList){
        _bannerLoading.value = false
        _pageLoading.value = false
        it.data
    }

    val name = _name
    val lastName = _lastName
    var likes = _likes
    var bannerLoading = _bannerLoading

    val pageLoading = _pageLoading

    fun loadBanner() {
        _bannerLoading.value = true
        refershTrigger.value = true
    }

    fun loadMore() {
        page.value = (page.value ?: 0) + 1
        _pageLoading.value = true
    }

    fun refresh() {
        loadBanner()
        page.value = 0
    }

    /**
     * Increments the number of likes.
     */
    fun onLike() {
        _likes.value = (_likes.value ?: 0) + 1
    }

    /**
     * Returns popularity in buckets: [Popularity.NORMAL], [Popularity.POPULAR] or [Popularity.STAR]
     */
    val popularity = Transformations.map(_likes) {
        when {
                it > 9 -> Popularity.STAR
                it > 4 -> Popularity.POPULAR
                else -> Popularity.NORMAL
            }
        }
}

enum class Popularity {
    NORMAL,
    POPULAR,
    STAR
}
