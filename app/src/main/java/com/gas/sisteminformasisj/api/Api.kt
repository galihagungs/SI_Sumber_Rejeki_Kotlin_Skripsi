package com.gas.sisteminformasisj.api

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.*


data class UserLog(
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("password")
    val password: String
)

data class GetData(
    @field:SerializedName("id_user")
    val id_user: Int?
)
data class CariData(
    @field:SerializedName("username")
    val id_user: String
)
data class CariBarang(
    @field:SerializedName("code")
    val code: String
)
data class LogOut(
    @field:SerializedName("token")
    val token: String,
    @field:SerializedName("id_user")
    val id_user: Int
)
data class tambahTransaksi(
    @field:SerializedName("tanggal")
    val tanggal: String
)

data class cariPenjualan(
    @field:SerializedName("id_transaksi")
    val id_transaksi : Int
)

data class tambahPenjualan(
    @field:SerializedName("id_transaksi")
    val id_transaksi : Int,
    @field:SerializedName("code")
    val code : Int,
    @field:SerializedName("nama_barang")
    val nama_barang : String,
    @field:SerializedName("harga")
    val harga: Int,
    @field:SerializedName("jumlah")
    val jumlah : Int
)

data class tambahUser(
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("password")
    val password: String,
    @field:SerializedName("nama")
    val nama: String,
    @field:SerializedName("roles")
    val roles: Int,
)
data class tambahBarang(
    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("nama_barang")
    val nama_barang: String,
    @field:SerializedName("stok")
    val stok: Int,
    @field:SerializedName("harga")
    val harga: Int,
)
data class UpdateBarang(
    @field:SerializedName("id_barang")
    val id_barang: Int,
    @field:SerializedName("code")
    val code: Int,
    @field:SerializedName("nama_barang")
    val nama_barang: String,
    @field:SerializedName("stok")
    val stok: Int,
    @field:SerializedName("harga")
    val harga: Int,
)

data class UpdateUser(
    @field:SerializedName("id_user")
    val id_user: Int,
    @field:SerializedName("username")
    val username: String,
    @field:SerializedName("nama")
    val nama: String,
    @field:SerializedName("roles")
    val roles: Int,
)
data class CetakLaporanBulan(
    @field:SerializedName("bulan")
    val bulan: Int,
    @field:SerializedName("tahun")
    val tahun: Int
)


interface ApiService{

    @POST("auth/api/v1/cetaktransaksi")
    fun cetakLaporanBulan(
        @Body cetakLaporanBulan: CetakLaporanBulan
    ): Call<CetakTransaksiResponse>

    @POST("auth/api/v1/caripenjualanbyid")
    fun cariPenjualan(
        @Body CariPenjualan: cariPenjualan
    ): Call<PDFResponse>

    @POST("auth/api/v1/getalltransaksiin")
    fun  getalltransaksi(): Call<GetTransaksiResponse>
    @POST("auth/api/v1/tambahbarang")
    fun tambahBarang(
        @Body tambahBarang: tambahBarang
    ): Call<TransaksiResponse>

    @POST("auth/api/v1/updatebarang")
    fun updateBarang(
        @Body updateBarang: UpdateBarang
    ): Call<TransaksiResponse>

    @POST("auth/api/v1/tambahpenjualan")
    fun tambahPenjualan(
        @Body tambahPenjualan: tambahPenjualan
    ): Call<TransaksiResponse>

    @POST("auth/api/v1/gettransaksiin")
    fun getIdTransaksi(
        @Body tambahTrans: tambahTransaksi
    ): Call<GetIdTransaksiResponse>

    @POST("auth/api/v1/tambahtransaksi")
    fun tambahTransaksi(
        @Body tambahTrans: tambahTransaksi
    ): Call<TransaksiResponse>

    @POST("auth/api/v1/getuser")
    fun getUser(
        @Body getData: GetData
    ): Call<GetUserResponse>

    @POST("auth/api/v1/caribarang")
    fun getBarang(
        @Body code: CariBarang
    ):Call<GetBarangResponse>

    @GET("auth/api/v1/allbarang")
    fun getAllBarang(
    ): Call<GetBarangResponse>

    @POST("auth/api/v1/tambahuser")
    fun tambahUser(
        @Body user: tambahUser
    ):Call<UpdateResponse>

    @POST("auth/api/v1/ubahuser")
    fun updateUser(
        @Body user: UpdateUser
    ): Call<UpdateResponse>

    @POST("auth/api/v1/login")
    fun login(
        @Body user: UserLog
    ): Call<LoginResponse>

    @GET("auth/api/v1/cektoken")
    fun cekToken(
        @Header("Authorization") token: String,
        @Query("id_user") id_user: Int
    ): Call<CekUserResponse>

    @POST("auth/api/v1/logout")
    fun logout(
        @Body logout: LogOut
    ): Call<CekUserResponse>

    @DELETE("auth/api/v1/hapusbarang/{id_barang}")
    fun deleteBarang(
        @Path("id_barang") id_barang: Int
    ): Call<DeleteResponse>

    @DELETE("auth/api/v1/hapususer/{id_user}")
    fun deleteUser(
        @Path("id_user") id_user: Int
    ): Call<DeleteResponse>

    @POST("auth/api/v1/hapususer")
    fun searchUser(
        @Body cariData: CariData
    ): Call<GetUserResponse>

    @POST("auth/api/v1/getalluser")
    fun getAllUser(): Call<GetUserResponse>

    @GET("auth/api/v1/allbarang")
    fun getAllBarangStok(): Call<GetBarangResponse>
}

class ApiConfig {
    fun getApiService(): ApiService {
        val loggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
//            .baseUrl("http://192.168.1.6:3000/")
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }
}