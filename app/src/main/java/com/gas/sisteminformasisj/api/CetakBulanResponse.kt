package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName

data class CetakBulanResponse(

	@field:SerializedName("values")
	val values: List<CetakBulan>,

	@field:SerializedName("status")
	val status: Int
)

data class CetakBulan(

	@field:SerializedName("TAHUN")
	val tAHUN: Int,

	@field:SerializedName("id_transaksi")
	val idTransaksi: Int,

	@field:SerializedName("BULAN")
	val bULAN: Int
)
