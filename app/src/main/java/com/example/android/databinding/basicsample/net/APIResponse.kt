package com.example.android.databinding.basicsample.net

/**
 *  作者：xiaofei
 *  日期：2019/10/16
 */
data class APIResponse<T>(var data: T?, var errorCode: Int, var errorMsg: String)