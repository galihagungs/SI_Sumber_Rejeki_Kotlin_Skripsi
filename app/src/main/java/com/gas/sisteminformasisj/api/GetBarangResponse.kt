package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class GetBarangResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("values")
	val values: List<BarangItem>
)

data class BarangItem(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("id_barang")
	val idBarang: Int,

	@field:SerializedName("nama_barang")
	val namaBarang: String,

	@field:SerializedName("stok")
	val stok: Int
)
