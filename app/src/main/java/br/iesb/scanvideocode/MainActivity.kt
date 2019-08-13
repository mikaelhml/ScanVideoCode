package br.iesb.scanvideocode

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;



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
            val w = 15
            val h = 15

            val conf = Bitmap.Config.ARGB_8888 // see other conf types
            val bitmap = Bitmap.createBitmap(w, h, conf)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
            val image = stream.toByteArray()
            criarQRCode(image)
        }
    }

    private fun criarQRCode(string64: ByteArray, b: Boolean = false) {
        if(!b){
            carregarImg()
        }
        else{
            gerarQR(string64)
        }



    }

    @SuppressLint("ShowToast")
    private fun gerarQR(string64: ByteArray) {
        val writer = QRCodeWriter()
        val string = " teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste te"
        img_QRCode as ImageView
        try {
            val bitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, 512, 512)
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
            Toast.makeText(this,"Fuck",Toast.LENGTH_LONG)
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 111 && resultCode == RESULT_OK) {
            val selectedFile = data?.data //The uri with the location of the file
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedFile)
            val string64 = convert(bitmap);
            criarQRCode(string64,true)
        }
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Está vazio", Toast.LENGTH_LONG).show()
            } else {


                val options = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeByteArray(result.contents.toByteArray(), 0, result.contents.toString().length, options)
                img_QRCode.setImageBitmap(bitmap)
                Toast.makeText(this, result.contents.toString(), Toast.LENGTH_LONG).show()


            }

        } else {

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun convert(bitmap: Bitmap): ByteArray {
        //val outputStream = ByteArrayOutputStream()
        //bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        //return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        val image = stream.toByteArray()
        return image

    }

    @Throws(IllegalArgumentException::class)
    fun desconverte(base64Str: String): Bitmap {
        val decodedBytes = Base64.decode(
            base64Str.substring(base64Str.indexOf(",") + 1),
            Base64.DEFAULT
        )

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }



}



