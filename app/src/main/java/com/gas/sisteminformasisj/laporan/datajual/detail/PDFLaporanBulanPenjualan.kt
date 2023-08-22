package com.gas.sisteminformasisj.laporan.datajual.detail

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.gas.sisteminformasisj.api.*
import com.gas.sisteminformasisj.laporan.datajual.*
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class PDFLaporanBulanPenjualan (private val mContex: Context) {

    private lateinit var CetakModelArrayList: DataCetakLaporan

    fun getData(selectedTahun: Int, selectedBulan: Int) {
        val data = CetakLaporanBulan(selectedBulan,selectedTahun)
        val client = ApiConfig().getApiService().cetakLaporanBulan(data)
        client.enqueue(object : Callback<CetakTransaksiResponse> {
            override fun onResponse(
                call: Call<CetakTransaksiResponse>,
                response: Response<CetakTransaksiResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
                    Log.d(ContentValues.TAG, "onSuccessPDF: ${resBody?.values}")
                    val size  = resBody?.values?.size
                    val data2 = ArrayList<TransaksiModel>()
                    for (data in resBody?.values!!){
//                        CetakModelArrayList = DataCetakLaporan(data.idTransaksi,data.tanggal)
//                        data2.add(TransaksiModel(data.idTransaksi,data.tanggal,"asdasd"))
//                        cari(data2)
//                        savePDF(data2,resBody.values)
                        val dataTrans =resBody.values
                        val id = cariPenjualan(data.idTransaksi)
                        val client = ApiConfig().getApiService().cariPenjualan(id)
                        val list: MutableList<String> = ArrayList()
                        val list2: MutableList<Int> = ArrayList()
                        client.enqueue(object : Callback<PDFResponse> {
                            override fun onResponse(
                                call: Call<PDFResponse>,
                                response: Response<PDFResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val resBody = response.body()
                                    for (i in resBody?.values!!){
                                        list.add(i.namaBarang)
                                        list2.add(i.harga * i.jumlah)
                                    }
                                    var newString = list.joinToString(
                                        separator = ","
                                    )
//                                    Log.d(ContentValues.TAG, "Join String: $newString")
                                    val total = list2.sum()
                                    data2.add(TransaksiModel(data.idTransaksi,data.tanggal,newString,total))
                                    savePDF(data2,dataTrans)

                                } else {
                                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                                }
                            }

                            override fun onFailure(call: Call<PDFResponse>, t: Throwable) {
                                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
                            }

                        })
                    }
                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CetakTransaksiResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })


    }

    fun savePDF(dataModel: ArrayList<TransaksiModel>, dataTrans: List<Transkasi>, ) {

//        Log.d(ContentValues.TAG, "Join String: ${dataModel.size}")
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val currentDate = sdf.format(Date())

        var now = Date()
        val filename = "Test.pdf"
        val path = File(mContex.getExternalFilesDir(null)!!.absolutePath+"/Test")

        if (!path.exists())path.mkdir()

        var file: File = File(path, filename)
        if (file.exists()){
            file.delete()
            file = File(path,filename)
        }
        val document = Document()

        PdfWriter.getInstance(document, FileOutputStream(file))

        document.open()
        document.pageSize = PageSize.A4
        document.addCreationDate()
        document.addAuthor("")
        document.addCreator("")

        val mColorAccent = BaseColor(0,153,204,255)
        val mHeadingFontSize = 20.0f
        val mValueFonSize = 26.9f

//        val fontRoboto = BaseFont.createFont("res/font/robot.ttf","UTF-8",BaseFont.EMBEDDED)

        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)

//        //font
//        val fontRobotoHeader = Font(
//            fontRoboto, 35.0f,Font.NORMAL,BaseColor.BLACK
//        )

        val paragraph1 = Paragraph(Chunk("Toko Sumber Rejeki"))
        paragraph1.alignment = Element.ALIGN_LEFT
        document.add(paragraph1)
        //sparator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        val paragraph2 = Paragraph(Chunk("Laporan Penjualan Bulanan"))
        paragraph2.alignment = Element.ALIGN_LEFT
        document.add(paragraph2)

        val paragraph3 = Paragraph(Chunk("Laporan Dicetak : $currentDate"))
        paragraph2.alignment = Element.ALIGN_LEFT
        document.add(paragraph3)

        //sparator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        val columnWidths = floatArrayOf(1f, 5f, 5f, 5f)
        val pdfPTable = PdfPTable(4)
        pdfPTable.spacingBefore= 20f
        pdfPTable.spacingAfter= 20f
        pdfPTable.setWidths(columnWidths)

        var cell: PdfPCell
        //Create cells
        val pdfPCell1 = PdfPCell(Paragraph("No"))
        val pdfPCell2 = PdfPCell(Paragraph("No. Invoice"))
        val pdfPCell3 = PdfPCell(Paragraph("List Barang"))
        val pdfPCell4 = PdfPCell(Paragraph("Total"))

        //Add cells to table
        pdfPTable.addCell(pdfPCell1)
        pdfPTable.addCell(pdfPCell2)
        pdfPTable.addCell(pdfPCell3)
        pdfPTable.addCell(pdfPCell4)
        //Add cells to table

//        for (i in dataTrans.indices) {
//            val pos = i + 1
//            pdfPTable.addCell(PdfPCell(Paragraph(pos.toString())))
//            pdfPTable.addCell(PdfPCell(Paragraph("SJ/002/${dataTrans[i].idTransaksi}")))
//            pdfPTable.addCell(PdfPCell(Paragraph(dataTrans[i].)))
//            pdfPTable.addCell(PdfPCell(Paragraph("adsasd")))
//        }
        for (i in dataModel.indices){
            val pos = i + 1
//            Log.d(ContentValues.TAG, "Pos: $pos String: ${dataModel[i].id_penjualan}")
            pdfPTable.addCell(PdfPCell(Paragraph(pos.toString())))
            pdfPTable.addCell(PdfPCell(Paragraph("SJ/002/${dataModel[i].id_penjualan}")))
            pdfPTable.addCell(PdfPCell(Paragraph(dataModel[i].namaBarang)))
            pdfPTable.addCell(PdfPCell(Paragraph("Rp.${dataModel[i].total}")))
        }


        document.add(pdfPTable)

        document.setMargins(250f, 250f, 100f, 100f);

        document.close()
    }




}