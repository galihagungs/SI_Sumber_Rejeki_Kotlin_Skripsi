package com.gas.sisteminformasisj.laporan.datajual

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.GetTransaksiResponse
import com.gas.sisteminformasisj.api.GetUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LihatDataJualViewModelApi: ViewModel() {

    private val _listJualData = MutableLiveData<GetTransaksiResponse?>()
    val ListJualData: MutableLiveData<GetTransaksiResponse?> = _listJualData

    fun setData(){
        val client = ApiConfig().getApiService().getalltransaksi()
        client.enqueue(object : Callback<GetTransaksiResponse> {
            override fun onResponse(
                call: Call<GetTransaksiResponse>,
                response: Response<GetTransaksiResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(ContentValues.TAG, "onSuccess: $resBody")

                    ListJualData.value = resBody
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GetTransaksiResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
}