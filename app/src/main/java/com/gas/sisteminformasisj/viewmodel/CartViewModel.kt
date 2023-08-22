package com.gas.sisteminformasisj.viewmodel

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gas.sisteminformasisj.api.GetBarangResponse
import com.gas.sisteminformasisj.kasir.dataClassKeranjang
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel: ViewModel() {
    private val _postCartItem = ArrayList<dataClassKeranjang>()
    val PostCartItem = MutableLiveData<List<dataClassKeranjang>>()

    fun addCart(data: dataClassKeranjang) {
        val cek = _postCartItem.contains(data)
        Log.d(ContentValues.TAG, "Contain View Model: $cek")
        if (cek == false){
            _postCartItem.add(data)
            PostCartItem.value = _postCartItem
        }else{
            Log.d(ContentValues.TAG, "Barang Sudah Ada")
        }
    }
}
