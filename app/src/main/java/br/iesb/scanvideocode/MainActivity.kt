package br.iesb.scanvideocode

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
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
    }
    private fun iniciaScan(){
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)

        if(result!=null){
            if(result.contents == null){
                Toast.makeText(this, "Está vazio",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(this,result.contents.toString(),Toast.LENGTH_LONG).show()
            }

        }
        else{

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}
