package com.gas.sisteminformasisj.laporan.datajual

import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.databinding.ActivityCetakPenjualanBinding
import com.gas.sisteminformasisj.laporan.LaporanMainActivity
import com.gas.sisteminformasisj.laporan.datajual.detail.DetailTransaksiActivity
import com.gas.sisteminformasisj.laporan.datajual.detail.PDFLaporanBulanPenjualan
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import java.util.*

class CetakPenjualanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCetakPenjualanBinding
    private var selectedTahun = 2022
    private var selectedBulan = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCetakPenjualanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        selecteddataspinerTahun()
        selecteddataspinerBulan()
        binding.Back.setOnClickListener {
            startActivity(Intent(this, LaporanMainActivity::class.java))
            finish()
        }
        binding.CetakLaporan.setOnClickListener {
            Log.d(TAG, "Spiner Cetak: Tahun: $selectedTahun Bulan: $selectedBulan")
            requsetStorage()
            change()
        }

    }
    private fun change(){
        val handler = Handler()
        handler.postDelayed({
            // do something after 1000ms
            val intent = Intent(this, DetailTransaksiActivity::class.java)
            startActivity(intent)
        }, 2000)
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

    private fun selecteddataspinerTahun(){
        val rolesItemSpinner = resources.getStringArray(R.array.Tahun)
        val spinner = findViewById<Spinner>(R.id.spinner_roles)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesItemSpinner)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    when (spinner.selectedItem) {
                        "2022" -> {
                            selectedTahun = 2022
                        }
                        "2023" -> {
                            selectedTahun = 2023
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun selecteddataspinerBulan(){
        val rolesItemSpinner = resources.getStringArray(R.array.Bulan)
        val spinner = findViewById<Spinner>(R.id.spinner_roles2)
        if (spinner != null) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, rolesItemSpinner)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    when (spinner.selectedItem) {
                        "Januari" -> {
                            selectedBulan = 1
                        }
                        "Februari" -> {
                            selectedBulan = 2
                        }
                        "Maret" -> {
                            selectedBulan = 3
                        }
                        "April" -> {
                            selectedBulan = 4
                        }
                        "Mei" -> {
                            selectedBulan = 5
                        }
                        "Juni" -> {
                            selectedBulan = 6
                        }
                        "Juli" -> {
                            selectedBulan = 7
                        }
                        "Agustus" -> {
                            selectedBulan = 8
                        }
                        "September" -> {
                            selectedBulan = 9
                        }
                        "Oktober" -> {
                            selectedBulan = 10
                        }
                        "November" -> {
                            selectedBulan = 11
                        }
                        "Desember" -> {
                            selectedBulan = 12
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
    }

    private fun PrintPDF(){
        val pdf = PDFLaporanBulanPenjualan(this)
        pdf.getData(selectedTahun,selectedBulan)
    }

    private fun requsetStorage() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                        PrintPDF()
                    }
                    if (report.isAnyPermissionPermanentlyDenied){

                    }

                }
                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            }).withErrorListener{
                Toast.makeText(applicationContext,"ErrorOccired!", Toast.LENGTH_SHORT).show()
            }.onSameThread().check()
    }

}