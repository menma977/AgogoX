package id.co.agogo

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import id.co.agogo.dialog.BottomPopUpNavigationMenu

class LoginActivity : AppCompatActivity(), BottomPopUpNavigationMenu.EditTextChangeListener {
    override fun onEditTextChange(text: String) {
        if (text.isNotEmpty()) {

        }
    }

    private lateinit var version: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button

    private var versionName: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        version = findViewById(R.id.version)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.loginButton)

        versionName = try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            "${this.getString(R.string.version)} ${pInfo.versionName}"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            "${this.getString(R.string.version)} 0"
        }

        version.text = versionName

        username.requestFocus()

        login.setOnClickListener {
            val popUpDialog = BottomPopUpNavigationMenu()
            popUpDialog.show(supportFragmentManager, "Password TRX")
        }
    }
}
