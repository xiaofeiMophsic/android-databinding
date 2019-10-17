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

package com.example.android.databinding.basicsample.ui

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.bingoogolapple.bgabanner.BGABanner
import com.example.android.databinding.basicsample.R
import com.example.android.databinding.basicsample.data.SimpleViewModel
import com.example.android.databinding.basicsample.databinding.PlainActivityBinding
import com.example.android.databinding.basicsample.model.Banner
import com.example.android.databinding.basicsample.ui.adapter.ArticleAdapter
import com.example.android.databinding.basicsample.util.displayWithUrl
import kotlinx.android.synthetic.main.plain_activity.*

/**
 * Plain old activity with lots of problems to fix.
 */
class PlainOldActivity : AppCompatActivity() {

    // Obtain ViewModel from ViewModelProviders
    private val viewModel by lazy { ViewModelProviders.of(this).get(SimpleViewModel::class.java) }
    private lateinit var articleAdapter: ArticleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setContentView(R.layout.plain_activity)
        val binding: PlainActivityBinding = DataBindingUtil.setContentView(this, R.layout.plain_activity)
        // TODO: Explicitly setting initial values is a bad pattern. We'll fix that.
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this

        binding.refreshLayout.autoRefresh()

        val liveString: MutableLiveData<String> = MutableLiveData()
        liveString.observe(this, Observer { str->
            Log.i("TAG", str)
        })

        liveString.postValue("hi")

        binding.run {
            val bannerAdapter = BGABanner.Adapter<ImageView, Banner>{_, image, model, _->
                image.displayWithUrl(this@PlainOldActivity, model?.imagePath)
            }
            banner.setAdapter(bannerAdapter)
            viewmodel?.banners?.observe(this@PlainOldActivity, Observer {
                banner.setData(it, null)
            })

//            viewmodel?.loadMore()
        }
        binding.recyclerView.let {
            articleAdapter = ArticleAdapter(this)
            it.adapter = articleAdapter
            it.layoutManager = LinearLayoutManager(this)
            it.addItemDecoration(DividerItemDecoration(this, (it.layoutManager as LinearLayoutManager).orientation))
        }
        binding.viewmodel?.articles?.observe(this, Observer {
            it.run {
                articleAdapter.submitList(datas)
            }
        })
    }
}

