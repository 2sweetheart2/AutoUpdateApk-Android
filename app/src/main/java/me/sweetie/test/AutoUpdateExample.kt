package me.sweetie.test

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import me.sweetie.autoupdate.AutoUpdate


class AutoUpdateExample : AppCompatActivity() {

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_autoupdates)
        checkPermission(PERMISSIONS_EXTERNAL_STORAGE[1], REQUEST_EXTERNAL_PERMISSION_CODE)
    }

    val REQUEST_EXTERNAL_PERMISSION_CODE = 666;

    var PERMISSIONS_EXTERNAL_STORAGE = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@AutoUpdateExample,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {

            ActivityCompat.requestPermissions(
                this@AutoUpdateExample,
                PERMISSIONS_EXTERNAL_STORAGE,
                requestCode
            )
        } else {
            Toast.makeText(this@AutoUpdateExample, "Permission already granted", Toast.LENGTH_SHORT)
                .show()
            val AU = AutoUpdate("url", BuildConfig.VERSION_CODE,null, "response")
            AU.getUpdate(supportFragmentManager,false)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_EXTERNAL_PERMISSION_CODE) {
            val AU = AutoUpdate("url", BuildConfig.VERSION_CODE,null, "response")
            AU.getUpdate(supportFragmentManager,true)

        }
    }


}


