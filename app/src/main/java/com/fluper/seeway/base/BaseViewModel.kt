package com.fluper.seeway.base

import androidx.lifecycle.ViewModel
import com.fluper.seeway.networks.ApiInterface
import com.fluper.seeway.networks.RetrofitUtil

abstract class BaseViewModel: ViewModel() {

    val apiInterface : ApiInterface by lazy {
        RetrofitUtil.createBaseApiService()
    }
}