package com.gas.sisteminformasisj.stok

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.BarangModelAPI
import com.gas.sisteminformasisj.api.TransaksiResponse
import com.gas.sisteminformasisj.api.UpdateBarang
import com.gas.sisteminformasisj.databinding.ActivityEditBarangBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditBarangActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBarangBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBarangBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setData()
        binding.BackStok.setOnClickListener {
            backtoStok()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun setData(){
        val dataBarang = intent.extras?.getParcelableArrayList<BarangModelAPI>("DataBarang")
        for (data in dataBarang!!){
            binding.editTextTextBarcode.setText(data.code)
            binding.editTextTextNama.setText(data.namabarang)
//            binding.editTextTextHarga.setText(data.harga)
            binding.editTextTextHarga.setText(data.harga.toString())
            binding.editTextBarang.setText(data.stok.toString())
            binding.buttonUbahData.setOnClickListener {
                updateData(data.id_barang)
            }
        }
    }
    private fun updateData(idBarang: Int) {
        val code = binding.editTextTextBarcode.text.toString()
        val namaBarang = binding.editTextTextNama.text.toString()
        val harga = binding.editTextTextHarga.text.toString()
        val stok = binding.editTextBarang.text.toString()

        val update = UpdateBarang(idBarang,code.toInt(),namaBarang,stok.toInt(),harga.toInt())
        Log.d(ContentValues.TAG, "Update Array: $update")
        val client = ApiConfig().getApiService().updateBarang(update)
        client.enqueue(object : Callback<TransaksiResponse> {
            override fun onResponse(
                call: Call<TransaksiResponse>,
                response: Response<TransaksiResponse>
            ) {
                val resBody = response.body()
                if (resBody?.status == 200){
                    Toast.makeText(this@EditBarangActivity, "Update Barang Berhasil", Toast.LENGTH_SHORT).show()
                    backtoStok()
                }else{
                    Toast.makeText(this@EditBarangActivity, "Update Barang Gagal", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
    private fun backtoStok(){
        startActivity(Intent(this, StokActivity::class.java))
        finish()
    }
}