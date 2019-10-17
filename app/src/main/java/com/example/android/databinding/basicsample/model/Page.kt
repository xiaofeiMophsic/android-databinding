package com.example.android.databinding.basicsample.model

/**
 *  作者：xiaofei
 *  日期：2019/10/17
 */
data class Page<T>(var curPage: Int,
                   var datas: List<T>,
                   var offset: Int,
                   var over: Boolean,
                   var pageCount: Int,
                   var size: Int,
                   var total: Int
                   )