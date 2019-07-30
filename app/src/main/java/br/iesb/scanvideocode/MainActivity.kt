package br.iesb.scanvideocode

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFunci()
    }

    private fun initFunci() {
        btnScanQRCode.setOnClickListener {
            iniciaScan()
        }
        btnCriarQRCode.setOnClickListener {
            criarQRCode()
        }
    }

    private fun criarQRCode() {
        carregarImg()
        val writer = QRCodeWriter()
        img_QRCode as ImageView
        try {
            val bitMatrix = writer.encode("teste", BarcodeFormat.QR_CODE, 512, 512)
            val width = 512
            val height = 512
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    if (!bitMatrix.get(x, y))
                        bmp.setPixel(x, y, Color.WHITE)
                    else
                        bmp.setPixel(x, y, Color.BLACK)
                }
            }
            //img_QRCode.setImageBitmap(bmp)
        } catch (e: WriterException) {
            //Log.e("QR ERROR", ""+e);
        }

    }

    private fun carregarImg() {
        val intent = Intent()
            .setType("*/*")
            .setAction(Intent.ACTION_GET_CONTENT)

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 111)
    }
    private fun iniciaScan() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedFile)
            img_QRCode.setImageBitmap(bitmap)
        }
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Está vazio", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, result.contents.toString(), Toast.LENGTH_LONG).show()
            }

        } else {

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun encoder(filePath: String): String{
        val bytes = File(filePath).readBytes()
        val base64 = Base64.getEncoder().encodeToString(bytes)
        return base64
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun decoder(base64Str: String, pathFile: String): Unit{
        val imageByteArray = Base64.getDecoder().decode(base64Str)
        File(pathFile).writeBytes(imageByteArray)
    }



}



