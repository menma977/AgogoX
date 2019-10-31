package id.co.agogo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import id.co.agogo.dialog.BottomPopUpNavigationMenu
import android.content.pm.PackageManager
import android.view.WindowManager
import android.widget.ProgressBar
import java.util.*
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity(), BottomPopUpNavigationMenu.EditTextChangeListener {
    private lateinit var progressBar: ProgressBar
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        progressBar = findViewById(R.id.loading_bar)
        version = findViewById(R.id.version)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
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
            val popUpDialog = BottomPopUpNavigationMenu()
            popUpDialog.show(supportFragmentManager, "Password TRX")
        }
    }
}
