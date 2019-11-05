package id.co.agogo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import id.co.agogo.ppob.pulsa.PulsaActivity
import java.util.*
import kotlin.concurrent.schedule

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
            goTo = Intent(this, PulsaActivity::class.java)
            startActivity(goTo)
        }

        more.setOnClickListener {
            goTo = Intent(this, MoreActivity::class.java)
            startActivity(goTo)
        }
    }
}
