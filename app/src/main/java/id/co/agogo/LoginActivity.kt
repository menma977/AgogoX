package id.co.agogo

import android.Manifest
import android.annotation.TargetApi
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import id.co.agogo.dialog.BottomPopUpNavigationMenu
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity(), BottomPopUpNavigationMenu.EditTextChangeListener {
    private lateinit var progressBar: ProgressBar
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var version: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private var passwordTRX: String = ""
    private var versionName: String = "0"

    override fun onEditTextChange(text: String) {
        username.clearFocus()
        username.isEnabled = false
        password.clearFocus()
        password.isEnabled = false
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        progressBar.progress = 0
        Timer().schedule(1000) {
            runOnUiThread {
                progressBar.progress = 50
                if (text.isNotEmpty()) {
                    passwordTRX = text
                    Timer().schedule(1000) {
                        println(passwordTRX)
                        runOnUiThread {
                            progressBar.progress = 100
                            username.isEnabled = true
                            password.isEnabled = true
                            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        }
                    }
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
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
                Manifest.permission.FOREGROUND_SERVICE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.USE_BIOMETRIC
                ), 100
            )
        }

        val executor = Executors.newSingleThreadExecutor()

        setContentView(R.layout.activity_login)
        progressBar = findViewById(R.id.loading_bar)
        version = findViewById(R.id.version)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

//        biometricPrompt = BiometricPrompt.Builder(this).setTitle("Finger Print")
//            .setSubtitle("plise place your finger").setDescription("this is finger print")
//            .setNegativeButton(
//                "Cancel",
//                executor,
//                DialogInterface.OnClickListener { dialog, which ->
//
//                }).build()

        progressBar.progress = 100
        versionName = try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            "${this.getString(R.string.version)} ${pInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "${this.getString(R.string.version)} 0"
        }

        version.text = versionName

        username.requestFocus()

        username.setOnClickListener {
            when {
                username.text.isEmpty() -> username.requestFocus()
                password.text.isEmpty() -> password.requestFocus()
                else -> {
                    val popUpDialog = BottomPopUpNavigationMenu()
                    popUpDialog.show(supportFragmentManager, "Password TRX")
                }
            }
        }

        password.setOnClickListener {
            //            biometricPrompt.authenticate(CancellationSignal(), executor, object : BiometricPrompt.AuthenticationCallback() {
//                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//                    super.onAuthenticationSucceeded(result)
//                    Timer().schedule(500) {
//                        runOnUiThread {
//                            Toast.makeText(applicationContext, "awwwwwwwwwwwwwww", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//
//                override fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                    Timer().schedule(500) {
//                        runOnUiThread {
//                            Toast.makeText(applicationContext, "xxxxxxxxxxxxxxxxxxxxxx", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                }
//            })
//            val popUpDialog = BottomPopUpNavigationMenu()
//            popUpDialog.show(supportFragmentManager, "Password TRX")
        }
    }
}
