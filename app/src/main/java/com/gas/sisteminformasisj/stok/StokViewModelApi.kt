package com.gas.sisteminformasisj.stok

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.GetBarangResponse
import com.gas.sisteminformasisj.api.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StokViewModelApi: ViewModel() {
    private val _stokData = MutableLiveData<GetBarangResponse?>()
    val stokData: MutableLiveData<GetBarangResponse?> = _stokData

    fun setData(){
        val client = ApiConfig().getApiService().getAllBarangStok()
        client.enqueue(object : Callback<GetBarangResponse> {
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(ContentValues.TAG, "onSuccess: $resBody")
                    _stokData.value = resBody
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}