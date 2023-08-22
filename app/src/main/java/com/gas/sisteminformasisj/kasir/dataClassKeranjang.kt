package com.gas.sisteminformasisj.kasir

import com.google.gson.annotations.SerializedName

data class dataClassKeranjang(

    @field:SerializedName("code")
    val code: String,

    @field:SerializedName("harga")
    val harga: Int,

    @field:SerializedName("id_barang")
    val idBarang: Int,

    @field:SerializedName("nama_barang")
    val namaBarang: String,

    @field:SerializedName("stok")
    var stok: Int,

    @field:SerializedName("jumlah")
    var jumlah: Int
)