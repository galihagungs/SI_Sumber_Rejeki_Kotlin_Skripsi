package com.gas.sisteminformasisj.laporan.datajual.detail

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.gas.sisteminformasisj.api.ApiConfig
import com.gas.sisteminformasisj.api.PDF
import com.gas.sisteminformasisj.api.PDFResponse
import com.gas.sisteminformasisj.api.cariPenjualan
import com.gas.sisteminformasisj.laporan.datajual.DataFilter
import com.gas.sisteminformasisj.laporan.datajual.DataPDFModel
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.util.*


class PDF(private val mContex: Context, data: DataFilter) {


    fun getData(datafil: DataFilter){
        val idtransaksi = datafil.getidtransaksi()
        val tanggal = datafil.getTanggaldiplay()

        val id = cariPenjualan(idtransaksi)
        val client = ApiConfig().getApiService().cariPenjualan(id)
        client.enqueue(object : Callback<PDFResponse> {
            override fun onResponse(
                call: Call<PDFResponse>,
                response: Response<PDFResponse>
            ) {
                if (response.isSuccessful) {
                    val resBody = response.body()
//                    Log.d(ContentValues.TAG, "Coba223: ${resBody?.values}")

                    val size  = resBody?.values?.size
                    val data2 = ArrayList<DataPDFModel>()
                    val list: MutableList<String> = ArrayList()
                    for (data in resBody?.values!!){
                        data2.add(DataPDFModel(data.idPenjualan,data.idTransaksi,data.code,data.namaBarang,data.harga,data.jumlah))
                    }
                    savePDF(idtransaksi,tanggal,resBody?.values)


                } else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PDFResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "onFailure: ${t.message.toString()}")
            }

        })


    }


    fun savePDF(idtransaksi: Int, tanggal: String, values: List<PDF>) {

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

        PdfWriter.getInstance(document,FileOutputStream(file))

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

        val paragraph2 = Paragraph(Chunk("No. Invoice = INV/0$idtransaksi"))
        paragraph2.alignment = Element.ALIGN_LEFT
        document.add(paragraph2)

        //sparator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        val paragraph3 = Paragraph(Chunk("Tanggal Pembelian : $tanggal"))
        paragraph3.add(Chunk(VerticalPositionMark()))
        paragraph3.add("Status Pembayaran: Berhasil")
        document.add(paragraph3)

        //sparator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        document.add(Paragraph(""))
        val columnWidths = floatArrayOf(1f, 5f, 5f,5f,5f)
        val pdfPTable = PdfPTable(5)
        pdfPTable.spacingBefore= 20f
        pdfPTable.spacingAfter= 20f
        pdfPTable.setWidths(columnWidths)

        var cell: PdfPCell
        //Create cells
        val pdfPCell1 = PdfPCell(Paragraph("No"))
        val pdfPCell2 = PdfPCell(Paragraph("Nama Barang"))
        val pdfPCell3 = PdfPCell(Paragraph("Harga Satuan"))
        val pdfPCell4 = PdfPCell(Paragraph("Jumlah"))
        val pdfPCell5 = PdfPCell(Paragraph("Total"))
        //Add cells to table

        //Add cells to table
        pdfPTable.addCell(pdfPCell1)
        pdfPTable.addCell(pdfPCell2)
        pdfPTable.addCell(pdfPCell3)
        pdfPTable.addCell(pdfPCell4)
        pdfPTable.addCell(pdfPCell5)

        Log.d(ContentValues.TAG, "onasdas: ${values.size}")

        for (k in values.indices){
            val pos = k + 1
            val total = values[k].harga * values[k].jumlah
            pdfPTable.addCell(PdfPCell(Paragraph(pos.toString())))
            pdfPTable.addCell(PdfPCell(Paragraph(values[k].namaBarang)))
            pdfPTable.addCell(PdfPCell(Paragraph("Rp.${values[k].harga}")))
            pdfPTable.addCell(PdfPCell(Paragraph("${values[k].jumlah}")))
            pdfPTable.addCell(PdfPCell(Paragraph("Rp. $total")))
        }

        document.add(pdfPTable)
        val totalharga = values.sumOf { it.harga * it.jumlah }

        val paragraph4 = Paragraph(Chunk("Total Harga = $totalharga"))
        paragraph4.alignment = Element.ALIGN_RIGHT
        document.add(paragraph4)

        val paragraph5 = Paragraph(Chunk("Terima Kasih Atas Pembelian Anda"))
        paragraph5.alignment = Element.ALIGN_LEFT
        document.add(paragraph5)

        //sparator
        document.add(Paragraph(""))
        document.add(Chunk(lineSeparator))
        document.add(Paragraph(""))

        document.setMargins(250f, 250f, 100f, 100f);

        document.close()
    }
}