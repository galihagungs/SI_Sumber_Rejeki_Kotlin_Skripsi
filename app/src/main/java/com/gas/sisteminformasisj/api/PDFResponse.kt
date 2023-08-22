package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class PDFResponse(

	@field:SerializedName("values")
	val values: List<PDF>,

	@field:SerializedName("status")
	val status: Int
)

data class PDF(

	@field:SerializedName("id_penjualan")
	val idPenjualan: Int,

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("harga")
	val harga: Int,

	@field:SerializedName("jumlah")
	val jumlah: Int,

	@field:SerializedName("nama_barang")
	val namaBarang: String,

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int
)
