package com.fluper.seeway.base

import androidx.fragment.app.Fragment
import com.fluper.seeway.networks.ApiInterface
import com.fluper.seeway.networks.RetrofitUtil
import com.fluper.seeway.database.model.SharedPreferenceUtils

open class BaseFragment : Fragment() {
    val apiInterface : ApiInterface by lazy {
        RetrofitUtil.createBaseApiService()
    }
    val sharedPreference: SharedPreferenceUtils by lazy {
        SharedPreferenceUtils.getInstance(requireContext().applicationContext)
    }
}