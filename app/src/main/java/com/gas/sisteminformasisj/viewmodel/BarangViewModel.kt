package com.gas.sisteminformasisj.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.GetBarangResponse
import com.gas.sisteminformasisj.api.GetData
import com.gas.sisteminformasisj.api.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BarangViewModel: ViewModel() {
    private  val _postValuesItemBarang = MutableLiveData<GetBarangResponse?>()
    val  postValuesItemBarang: MutableLiveData<GetBarangResponse?> = _postValuesItemBarang

    fun getAllBarang() {
        val client = ApiConfig().getApiService().getAllBarang()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                val resBody = response.body()
//                val value =  resBody?.values
                Log.e(ContentValues.TAG, "onSuccess: $resBody")
                _postValuesItemBarang.value = resBody
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}