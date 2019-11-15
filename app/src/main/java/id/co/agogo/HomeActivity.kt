package id.co.agogo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.co.agogo.ppob.pulsa.PulsaActivity
import java.util.*
import kotlin.concurrent.schedule
import android.net.ConnectivityManager
import android.widget.*
import id.co.agogo.api.BalanceController
import id.co.agogo.model.Session
import id.co.agogo.ppob.dana.DanaActivity
import id.co.agogo.ppob.eMoneyMandiri.EMoneyMandiriActivity
import id.co.agogo.ppob.gopay.GoPayActivity
import id.co.agogo.ppob.grab.GrabActivity
import id.co.agogo.ppob.ovo.OvoActivity
import id.co.agogo.ppob.pln.PlnActivity
import id.co.agogo.ppob.pulsaPascabayar.PulsaPascabayarActivity
import id.co.agogo.ppob.tabCashBNI.TabCashBNIActivity
import java.text.NumberFormat

/**
 * @property goTo Intent?
 * @property session Session
 * @property progressbarHome ProgressBar
 */
class HomeActivity : AppCompatActivity() {

    private var goTo: Intent? = null
    private lateinit var session: Session
    private lateinit var progressbarHome: ProgressBar

    /**
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = Session(this)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        progressbarHome = findViewById(R.id.progressBarHome)
        val balance: TextView = findViewById(R.id.balanceTextView)
        val logoutButton: ImageButton = findViewById(R.id.logoutButton)
        val refresh: ImageButton = findViewById(R.id.refreshButton)
        val more: ImageButton = findViewById(R.id.moreButton)
        val pln: ImageButton = findViewById(R.id.plnButton)
        val pulsa: ImageButton = findViewById(R.id.pulsaButton)
        val pulsaPSC: ImageButton = findViewById(R.id.pulsaPSCButton)
        val dana: ImageButton = findViewById(R.id.danaButton)
        val goPay: ImageButton = findViewById(R.id.goPayButton)
        val grab: ImageButton = findViewById(R.id.grabButton)
        val ovo: ImageButton = findViewById(R.id.ovoButton)
        val eMoneyMandiri: ImageButton = findViewById(R.id.eMoneyMandiriButton)
        val tabCashBNI: ImageButton = findViewById(R.id.tabCashBNIButton)

        progressbarHome.visibility = ProgressBar.VISIBLE

        Timer().schedule(500, 5000) {
            runOnUiThread {
                balance.text = numberFormat.format(getBalance())
            }
        }

        Timer().schedule(1000) {
            runOnUiThread {
                progressbarHome.visibility = ProgressBar.GONE
            }
        }

        logoutButton.setOnClickListener {
            Session(this).clear()
            goTo = Intent(this, MainActivity::class.java)
            startActivity(goTo)
            finishAndRemoveTask()
        }

        refresh.setOnClickListener {
            progressbarHome.visibility = ProgressBar.VISIBLE

            Timer().schedule(1000) {
                runOnUiThread {
                    balance.text = numberFormat.format(getBalance())
                    progressbarHome.visibility = ProgressBar.GONE
                }
            }
        }

        pln.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, PlnActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
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

        pulsaPSC.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, PulsaPascabayarActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        dana.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, DanaActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        grab.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, GrabActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        goPay.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, GoPayActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ovo.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, OvoActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        eMoneyMandiri.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, EMoneyMandiriActivity::class.java)
                startActivity(goTo)
            } else {
                Toast.makeText(
                    this,
                    "anda berada di koenksi yang tidak setabil",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        tabCashBNI.setOnClickListener {
            if (isNetworkConnected()) {
                goTo = Intent(this, TabCashBNIActivity::class.java)
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

    /**
     * @return Boolean
     */
    private fun isNetworkConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return cm.activeNetworkInfo != null && cm.activeNetworkInfo.isConnected
    }

    /**
     * @return Int
     */
    private fun getBalance(): Int {
        val response = BalanceController.Get(
            session.getString("username").toString(),
            session.getString("token").toString()
        ).execute().get()
        return if (response["Status"].toString() == "0") {
            val balance = response["Saldo"].toString().replace(".", "")
            balance.toInt()
        } else {
            0
        }
    }
}
