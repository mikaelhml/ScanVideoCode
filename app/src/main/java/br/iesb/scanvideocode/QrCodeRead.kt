package br.iesb.scanvideocode

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wonderkiln.camerakit.CameraView

class QrCodeRead : AppCompatActivity() {

    CameraView cameraView
    Button btn_capturar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr_code_read)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        cameraView = (CameraView)findViewById(R.id.cameraview)

    }
}
