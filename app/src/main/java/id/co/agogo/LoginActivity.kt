package id.co.agogo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import id.co.agogo.api.SessionTokenController
import id.co.agogo.dialog.BottomPopUpNavigationMenu
import id.co.agogo.model.Session
import java.lang.Exception
import java.util.*
import kotlin.concurrent.schedule

class LoginActivity : AppCompatActivity(), BottomPopUpNavigationMenu.ButtonClickListener {

    private lateinit var version: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var login: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var session: Session
    private var versionName: String = "0"

    private fun openPopUp() {
        progressBar.visibility = ProgressBar.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun closePopUp() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = ProgressBar.GONE
    }

    override fun onButtonClick(text: String) {
        if (text.isNotEmpty()) {
            Timer().schedule(500) {
                runOnUiThread {
                    openPopUp()
                }
            }
            Timer().schedule(1000) {
                val response =
                    SessionTokenController.Verification(
                        username.text.toString(),
                        session.getString("key").toString()
                    ).execute().get()
                println(response)
                if (response["Status"].toString() == "0") {
                    runOnUiThread {
                        session.saveString("token", response["IdLogin"].toString())
                        closePopUp()
                        Toast.makeText(
                            applicationContext,
                            response.getString("Pesan"),
                            Toast.LENGTH_LONG
                        ).show()
                        val goTo = Intent(applicationContext, HomeActivity::class.java)
                        finishAndRemoveTask()
                        startActivity(goTo)
                    }
                } else {
                    runOnUiThread {
                        closePopUp()
                        Toast.makeText(
                            applicationContext,
                            response.getString("Pesan"),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val goTo = Intent(this, MainActivity::class.java)
        finishAndRemoveTask()
        startActivity(goTo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        version = findViewById(R.id.version)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        login = findViewById(R.id.loginButton)
        progressBar = findViewById(R.id.progressBar)
        session = Session(this)

        progressBar.visibility = ProgressBar.GONE

        versionName = try {
            val pInfo = this.packageManager.getPackageInfo(packageName, 0)
            "${this.getString(R.string.version)} ${pInfo.versionName}"
        } catch (e: Exception) {
            e.printStackTrace()
            "${this.getString(R.string.version)} 0"
        }

        version.text = versionName

        username.requestFocus()

        login.setOnClickListener {
            openPopUp()
            Timer().schedule(1000) {
                val response =
                    SessionTokenController.Login(username.text.toString(), password.text.toString())
                        .execute().get()
                println(response)
                runOnUiThread {
                    if (response["Status"].toString() == "0") {
                        val popUpDialog = BottomPopUpNavigationMenu()
                        popUpDialog.show(supportFragmentManager, "Password TRX")
                        session.saveString("username", username.text.toString())
                        session.saveString("password", password.text.toString())
                        session.saveString("key", response["code_key"].toString())
                    } else {
                        Toast.makeText(
                            applicationContext,
                            response["Pesan"].toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    closePopUp()
                }
            }
        }
    }
}
