package com.gas.sisteminformasisj.stok

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.TransaksiResponse
import com.gas.sisteminformasisj.api.UpdateBarang
import com.gas.sisteminformasisj.api.tambahBarang
import com.gas.sisteminformasisj.databinding.ActivityMainBinding
import com.gas.sisteminformasisj.databinding.ActivityTambahStokBinding
import com.gas.sisteminformasisj.kasir.CaptureAct
import com.gas.sisteminformasisj.mainmenu.MainActivity
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TambahStokActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahStokBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahStokBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()

        binding.BackStok.setOnClickListener {
            startActivity(Intent(this, StokActivity::class.java))
            finish()
        }
        binding.QRscan.setOnClickListener {
            scanCode()
        }
        binding.buttonTambahBarang.setOnClickListener {
            setData()
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
        val code = binding.editTextTextBarcode.text.toString()
        val namaBarang = binding.editTextTextNama.text.toString()
        val harga = binding.editTextTextHarga.text.toString()
        val stok = binding.editTextBarang.text.toString()

        val tambah = tambahBarang(code.toInt(),namaBarang,stok.toInt(),harga.toInt())
        Log.d(TAG, "Update Array: $tambah")
        val client = ApiConfig().getApiService().tambahBarang(tambah)
        client.enqueue(object : Callback<TransaksiResponse> {
            override fun onResponse(
                call: Call<TransaksiResponse>,
                response: Response<TransaksiResponse>
            ) {
                val resBody = response.body()
                Log.d(TAG,"TambahBarang: $resBody")
                when (resBody?.status) {
                    200 -> {
                        Toast.makeText(this@TambahStokActivity, "Tambah Barang Berhasil", Toast.LENGTH_SHORT).show()
                        backtoStok()
                    }
                    201 -> {
                        Toast.makeText(this@TambahStokActivity, "Barang Sudah Ada Di Inventory", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this@TambahStokActivity, "Tambah Barang Gagal", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun scanCode() {
        val options = ScanOptions()
        options.setPrompt("Volume up to flash on")
        options.setBeepEnabled(true)
        options.setOrientationLocked(true)
        options.captureActivity = CaptureAct::class.java
        barLaucher.launch(options)
    }



    var barLaucher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents != null) {
            binding.editTextTextBarcode.setText(result.contents)
        }
    }

    private fun backtoStok(){
        startActivity(Intent(this, StokActivity::class.java))
        finish()
    }
}