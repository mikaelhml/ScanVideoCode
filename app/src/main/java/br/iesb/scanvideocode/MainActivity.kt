package br.iesb.scanvideocode

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.BitmapFactory;
import java.io.*


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
            criarQRCode(image.toString(),false)
        }
    }


    private fun criarQRCode(string64: String, b: Boolean = false) {
        if(!b){
            carregarImg()
        }
        else{
            gerarQR(string64)
        }



    }

    @SuppressLint("ShowToast")
    private fun gerarQR(string64: String) {
        val writer = QRCodeWriter()
        val string = " teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste teste teste teste teste teste  teste teste teste t"
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
            //The uri with the location of the file
            carregarArquivo(data)
            //val string64 = convert(bitmap);
            //criarQRCode(data.getContent(),true)
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

    private fun carregarArquivo(data: Intent?) {
        val selectedImage = data!!.getData()
        val imageStream = contentResolver.openInputStream(selectedImage!!)
        val yourSelectedImage = BitmapFactory.decodeStream(imageStream)

        val blob = ByteArrayOutputStream()
        yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100 /* Ignored for PNGs */, blob)
        //val bitmapdata = blob.toByteArray()
        //val bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.size)


        val input: InputStream = data!!.data?.let { getContentResolver().openInputStream(it) }!!
        //val inputAsString = input.bufferedReader().use { it.readText() }
        val stringascii = getByteArrayStringAscii(input)

        val testeee = getByteArrayFromString(stringascii)

        //val bitmapdata2 =getByteArray(inputAsString)
        //val value = Base64.encodeToString(getByteArray(inputAsString), Base64.DEFAULT);
        //val bitmap3 = BitmapFactory.decodeByteArray(bitmapdata2, 0, bitmapdata2.size)

        val options = BitmapFactory.Options()
        //options.inJustDecodeBounds = true

        val bitmap = BitmapFactory.decodeByteArray(testeee, 0, testeee.size)


        //val bitmap = BitmapFactory.decodeStream(input, null, options)

        img_QRCode.setImageBitmap(bitmap)

    }

    fun getByteArrayFromString(string: String) : ByteArray{
        return string.toByteArray()
    }

    private fun getByteArrayStringAscii(input: InputStream): String {

        var x :Int
        val buffer = StringBuffer()
        while ((input.read()) != -1) {
            x = input.read()
            var b = "00$x"
            b = b.substring(b.length - 3, b.length)
            buffer.append(b.toInt().toChar())
        }
        return buffer.toString()
    }

    fun getByteArray(data: String): ByteArray {

        val byteArrayOutputStream = ByteArrayOutputStream()
        val out = DataOutputStream(byteArrayOutputStream)

        try {
            out.write(data.toByteArray())
            byteArrayOutputStream.flush()
            byteArrayOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return byteArrayOutputStream.toByteArray()

    }
}



