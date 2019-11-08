package id.co.agogo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import id.co.agogo.ppob.pulsa.PulsaActivity
import java.util.*
import kotlin.concurrent.schedule
import java.net.InetAddress
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class HomeActivity : AppCompatActivity() {

    private var goTo: Intent? = null
    private lateinit var progressbarHome: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        progressbarHome = findViewById(R.id.progressBarHome)
        val refresh: ImageButton = findViewById(R.id.refreshButton)
        val more: Button = findViewById(R.id.moreButton)
        val pulsa: Button = findViewById(R.id.pulsaButton)

        progressbarHome.visibility = ProgressBar.VISIBLE

        Timer().schedule(1000) {
            runOnUiThread {
                progressbarHome.visibility = ProgressBar.GONE
            }
        }

        refresh.setOnClickListener {
            progressbarHome.visibility = ProgressBar.VISIBLE

            Timer().schedule(1000) {
                runOnUiThread {
                    progressbarHome.visibility = ProgressBar.GONE
                }
            }
        }

        pulsa.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, PulsaActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        more.setOnClickListener {
            goTo = Intent(this, MoreActivity::class.java)
            startActivity(goTo)
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }
}
