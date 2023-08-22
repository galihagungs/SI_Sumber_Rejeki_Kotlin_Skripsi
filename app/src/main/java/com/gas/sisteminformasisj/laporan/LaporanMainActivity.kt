package com.gas.sisteminformasisj.laporan

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.gas.sisteminformasisj.R
import com.gas.sisteminformasisj.databinding.ActivityLaporanMainBinding
import com.gas.sisteminformasisj.databinding.ActivityMainBinding
import com.gas.sisteminformasisj.laporan.datajual.CetakPenjualanActivity
import com.gas.sisteminformasisj.laporan.datajual.LihatDataActivity

class LaporanMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLaporanMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLaporanMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        lihatDataPenjualMenu()
        CetakDataPenjualMenu()
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

    private fun lihatDataPenjualMenu(){
        binding.LihatDataJualButton.setOnClickListener {
            startActivity(Intent(this, LihatDataActivity::class.java))
//            finish()
        }
    }
    private fun CetakDataPenjualMenu(){
        binding.CetakDataButton.setOnClickListener {
            startActivity(Intent(this, CetakPenjualanActivity::class.java))
//            finish()
        }
    }
}