package com.gas.sisteminformasisj.kasir

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.databinding.ActivityKasirBinding
import com.gas.sisteminformasisj.databinding.TambahBarangAddBinding
import com.gas.sisteminformasisj.mainmenu.MainActivity
import com.gas.sisteminformasisj.viewmodel.CartViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class KasirActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKasirBinding
    private lateinit var adapter: ListAdapterBarang
    private lateinit var barangModelArrayList: ArrayList<SearchBarang>


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKasirBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.rvHeroes.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvHeroes.addItemDecoration(itemDecoration)

        setupView()
        tambahdata()
        backToMenu()
        barScanBTN()
//        BTNProses()

//        setData()
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setData(data2: ArrayList<BarangModel>) {
        for (data in data2) {
            var databaru = dataClassKeranjang(data.code, data.harga, data.id_barang, data.nama_barang, data.stok, data.jumlah)
            val cartViewModel = ViewModelProvider(
                this,
                ViewModelProvider.NewInstanceFactory()
            )[CartViewModel::class.java]
            cartViewModel.addCart(databaru)
            cartViewModel.PostCartItem.observe(this) { it ->
                Toast.makeText(this, "asdas $it", Toast.LENGTH_SHORT).show()
//                setList(it)
                barangModelArrayList = ArrayList<SearchBarang>()
                var codeBar = ""
                var jumlahBeli = 0
                for (data in it){
                    var status = true
                    for (detail in barangModelArrayList){
                        if (detail.getCode().contains(data.code)) {
                            Toast.makeText(this, "Status Barang Sudah Ada Di list", Toast.LENGTH_SHORT).show()
                            status = false
                        }
                    }
                    Log.d(ContentValues.TAG, "Cek: $status")
                    if (status == true){
                        Log.d(ContentValues.TAG, "Status : TRUE AJG")
                        barangModelArrayList.add(SearchBarang(data.idBarang,data.code,data.namaBarang,data.stok,data.harga,data.jumlah))
                    }else{
                        Log.d(ContentValues.TAG, "Status : FALSE AJG")
                    }
                    Log.d(ContentValues.TAG, "Data Array List Barang: $barangModelArrayList")

                    adapter = ListAdapterBarang(barangModelArrayList)
                    binding.rvHeroes.scrollToPosition(adapter.itemCount)
                    binding.rvHeroes.adapter = adapter
                    adapter.setOnItemClickCallback(object : ListAdapterBarang.OnItemClickCallback{
                        override fun onItemClicked(Jumlah: Int, Pos: Int, code: String) {
                            Log.d(ContentValues.TAG, "Ini WUJUD Kode Bar: $code")
                            Log.d(ContentValues.TAG, "Ini WUJUD Jumlah: $Jumlah")
                            Log.d(ContentValues.TAG, "Ini WUJUD Pos: $Pos")
                            it.find { it.code == code}?.jumlah = Jumlah
                        }
                    })
                }
                BTNProses(it)

            }

        }
    }
    private fun tambahdata(){
        binding.buttonTambahBarang.setOnClickListener {
            val dialog = Dialog(this)
            val dialogAlertCommonBinding =
                TambahBarangAddBinding.inflate(LayoutInflater.from(baseContext.applicationContext));
            dialog.setContentView(dialogAlertCommonBinding.root)
            val code = dialogAlertCommonBinding.inputCode.text
//            dialogAlertCommonBinding.buttonscan.setOnClickListener {
//                scanCode()
//            }
            dialogAlertCommonBinding.caribarang.setOnClickListener {
                if (code.isEmpty()){
                    dialogAlertCommonBinding.inputCode.error = "Masukan Kode"
                }else{
                    getCariDataDBCode(code)
                    dialog.dismiss()
                }
            }
            dialogAlertCommonBinding.batalCari.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }
    }
    private fun getCariDataDBCode(code: Editable) {
        val kodebar = CariBarang(code.toString())
//        Toast.makeText(this@KasirActivity, kodebar.toString(), Toast.LENGTH_SHORT).show()
        val client = ApiConfig().getApiService().getBarang(kodebar)
        client.enqueue(object : Callback<GetBarangResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                val resBody = response.body()
//                Toast.makeText(this@KasirActivity, resBody?.values.toString(), Toast.LENGTH_SHORT).show()
                if (resBody?.values!!.isEmpty()){
                    Log.d(ContentValues.TAG, "Barang tidak ditemukan")
                    Toast.makeText(this@KasirActivity, "Barang tidak ditemukan", Toast.LENGTH_SHORT).show()
                }else{
                    val data2 = ArrayList<BarangModel>()
                    Log.d(ContentValues.TAG, "Barang Ditemukan")
//                    Toast.makeText(this@KasirActivity, "Barang Ditemukan", Toast.LENGTH_SHORT).show()
                    for (data in resBody?.values!!){
                        data2.add(BarangModel(data.idBarang,data.code,data.namaBarang,data.stok,data.harga,0))
                    }
//
                    setData(data2)
                }
//                Log.d(ContentValues.TAG, resBody.toString())


            }
            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }
    private fun getCariDataQR(code: String) {
        val kodebar = CariBarang(code)
        Toast.makeText(this@KasirActivity, kodebar.toString(), Toast.LENGTH_SHORT).show()
        val client = ApiConfig().getApiService().getBarang(kodebar)
        client.enqueue(object : Callback<GetBarangResponse> {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onResponse(
                call: Call<GetBarangResponse>,
                response: Response<GetBarangResponse>
            ) {
                val resBody = response.body()

                Log.d(ContentValues.TAG, resBody.toString())
                val data2 = ArrayList<BarangModel>()
                for (data in resBody?.values!!){
                    data2.add(BarangModel(data.idBarang,data.code,data.namaBarang,data.stok,data.harga,0))
                }
//                Toast.makeText(this@KasirActivity, data2.toString(), Toast.LENGTH_SHORT).show()
                setData(data2)

            }
            override fun onFailure(call: Call<GetBarangResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    private fun backToMenu(){
        binding.BackMenuMainAdmin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun BTNProses(data: List<dataClassKeranjang>) {
        binding.buttonProcess.setOnClickListener {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            val formatted = current.format(formatter)
            val date = tambahTransaksi(formatted)
            Log.d(ContentValues.TAG, "Tanggal: $date}")

            val data3 = data.find { it.jumlah == 0 }
//            Toast.makeText(this@KasirActivity, date.toString(), Toast.LENGTH_SHORT).show()
//            Toast.makeText(this@KasirActivity, data3.toString(), Toast.LENGTH_SHORT).show()
            if (data3 == null){
                val builder = androidx.appcompat.app.AlertDialog.Builder(this@KasirActivity)
                builder.setMessage("Proses Pembelian ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, _ ->
                    val client = ApiConfig().getApiService().tambahTransaksi(date)
                    client.enqueue(object : Callback<TransaksiResponse> {
                        override fun onResponse(
                            call: Call<TransaksiResponse>,
                            response: Response<TransaksiResponse>
                        ) {
                            val resBody = response.body()
                            Log.d(ContentValues.TAG, "Res Body Transaksi: $resBody")
                            if (resBody?.status == 200){

                                setTransaksi(data, date)
                            }

                        }
                        override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                            Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                        }

                    })
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }else{
                Toast.makeText(this@KasirActivity, "Masukan Jumlah Barang", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setTransaksi(dataList: List<dataClassKeranjang>, date: tambahTransaksi) {
        Handler().postDelayed({
            val client = ApiConfig().getApiService().getIdTransaksi(date)
            client.enqueue(object : Callback<GetIdTransaksiResponse> {
                override fun onResponse(
                    call: Call<GetIdTransaksiResponse>,
                    response: Response<GetIdTransaksiResponse>
                ) {
                    val resBody = response.body()
                    val idTransaksi = resBody?.idTransaksi
                    Log.d(ContentValues.TAG, "Res Body Id Transaksi: $idTransaksi")
                    if (resBody?.success == true){
                        for (data in dataList){
                            val tambah = tambahPenjualan(idTransaksi!!,data.code.toInt(),data.namaBarang,data.harga,data.jumlah)
                            Log.d(ContentValues.TAG, "Data : $tambah")
                            val client = ApiConfig().getApiService().tambahPenjualan(tambah)
                            client.enqueue(object : Callback<TransaksiResponse> {
                                override fun onResponse(
                                    call: Call<TransaksiResponse>,
                                    response: Response<TransaksiResponse>
                                ) {
                                    val resBody = response.body()
                                    Log.d(ContentValues.TAG, "Status: ${resBody?.values}")
                                    if (resBody?.status == 200){
                                        val builder = AlertDialog.Builder(this@KasirActivity)
                                        builder.setMessage("Transaksi Berhasil")
                                            .setCancelable(false)
                                            .setPositiveButton("Ok") { dialog, id ->
                                                val intent = intent
                                                finish()
                                                startActivity(intent)
                                                dialog.dismiss()
                                            }
                                        val alert = builder.create()
                                        alert.show()

                                    }
                                }
                                override fun onFailure(call: Call<TransaksiResponse>, t: Throwable) {
                                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                                }

                            })
                        }


                    }



                }
                override fun onFailure(call: Call<GetIdTransaksiResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                }

            })
        }, 2000)

    }
    private fun barScanBTN(){
        binding.buttonQR.setOnClickListener {
            scanCode()
        }
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
            getCariDataQR(result.contents)
        }
    }


}