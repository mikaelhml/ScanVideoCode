package br.iesb.scanvideocode

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_main.*

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
            img_QRCode.setImageBitmap(bmp)
        } catch (e: WriterException) {
            //Log.e("QR ERROR", ""+e);
        }

    }

    private fun iniciaScan() {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

}
