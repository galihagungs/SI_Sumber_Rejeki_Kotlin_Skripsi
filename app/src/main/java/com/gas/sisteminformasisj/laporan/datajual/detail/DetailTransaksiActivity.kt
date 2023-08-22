package com.gas.sisteminformasisj.laporan.datajual.detail

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.gas.sisteminformasisj.BuildConfig
import com.gas.sisteminformasisj.admin.AdminActivity
import com.gas.sisteminformasisj.databinding.ActivityDetailTransaksiBinding
import com.gas.sisteminformasisj.laporan.datajual.LihatDataActivity
import java.io.File


class DetailTransaksiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailTransaksiBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailTransaksiBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        detail()
        binding.BackMenuMainAdmin.setOnClickListener {
            finish()
        }
        binding.shareBtn.setOnClickListener {
            sharePDF()
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

    private fun detail(){
        val filePDF = File(this.getExternalFilesDir(null)!!.absolutePath+"/Test/Test.pdf")
        binding.pdfView.fromFile(filePDF)
            .enableSwipe(true).swipeHorizontal(true).load()
    }
    private fun sharePDF() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.putExtra(Intent.EXTRA_STREAM,  uriFromFile(this,File(this.getExternalFilesDir(null)!!.absolutePath+"/Test/Test.pdf")))
        shareIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        shareIntent.type = "application/pdf"
        startActivity(Intent.createChooser(shareIntent, "share.."))
    }
    private fun uriFromFile(context: Context, file:File):Uri {
        return FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
    }
}