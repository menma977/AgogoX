package id.co.agogo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import id.co.agogo.model.Session

/**
 * @property login Button
 * @property version TextView
 * @property versionName String
 */
class MainActivity : AppCompatActivity() {
    private lateinit var login: Button
    private lateinit var version: TextView
    private var versionName: String = "0"

    /**
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        doRequestPermission()

        if (doRequestPermission()) {
            if (!Session(this).getString("token").isNullOrEmpty()) {
                val goTo = Intent(this, HomeActivity::class.java)
                finishAndRemoveTask()
                startActivity(goTo)
            }
        }

        login = findViewById(R.id.loginButton)
        version = findViewById(R.id.version)

        versionName = try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            "${this.getString(R.string.version)} ${pInfo.versionName}"
        } catch (e: Exception) {
            e.printStackTrace()
            "${this.getString(R.string.version)} 0"
        }

        version.text = versionName

        login.setOnClickListener {
            doRequestPermission()
            if (doRequestPermission()) {
                val goTo = Intent(this, LoginActivity::class.java)
//                val goTo = Intent(this, HomeActivity::class.java)
                finishAndRemoveTask()
                startActivity(goTo)
            } else {
                doRequestPermission()
            }
        }
    }

    /**
     * @return Boolean
     */
    private fun doRequestPermission(): Boolean {
        return if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_WIFI_STATE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                    ), 100
                )
                true
            } else {
                true
            }
        } else {
            false
        }
    }
}
