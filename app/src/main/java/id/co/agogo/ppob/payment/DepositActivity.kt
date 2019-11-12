package id.co.agogo.ppob.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.core.text.isDigitsOnly
import id.co.agogo.R
import id.co.agogo.api.ppob.DataController
import id.co.agogo.api.ppob.DepositController
import org.json.JSONObject
import java.lang.Exception
import java.text.NumberFormat
import java.util.*

class DepositActivity : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar

    override fun onBackPressed() {
        super.onBackPressed()
        finishAndRemoveTask()
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deposit)

        val feeEditText: EditText = findViewById(R.id.feeEditText)
        val finish: Button = findViewById(R.id.finishButton)
        val phoneNumber: TextView = findViewById(R.id.phoneNumberTextView)
        val code: TextView = findViewById(R.id.codeTextView)
        val priceTextView: TextView = findViewById(R.id.priceTextView)
        val firstBalanceTextView: TextView = findViewById(R.id.firstBalanceTextView)
        val remainingBalanceTextView: TextView = findViewById(R.id.remainingBalanceTextView)
        progressBar = findViewById(R.id.progressBarDeposit)
        openPopUp()

        val responseIntent = JSONObject(intent.getSerializableExtra("response").toString())
        println(intent.getSerializableExtra("mobile").toString())

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        try {
            phoneNumber.text = responseIntent["NoHP"].toString()
            code.text = responseIntent["Kode"].toString()
            priceTextView.text = numberFormat.format(responseIntent["Harga"].toString().toInt())
            firstBalanceTextView.text =
                numberFormat.format(responseIntent["SaldoAwal"].toString().toInt())
            remainingBalanceTextView.text =
                numberFormat.format(responseIntent["SisaSaldo"].toString().toInt())
        } catch (e: Exception) {
            finishAndRemoveTask()
            Toast.makeText(
                this,
                "Terjadi kesalahan saat membaca data mungkin konkesi terputus pada peroses pembacaan data",
                Toast.LENGTH_LONG
            ).show()
        }

        closePopUp()

        feeEditText.requestFocus()

        finish.setOnClickListener {
            openPopUp()
            if (feeEditText.text.isEmpty()) {
                Toast.makeText(this, "Fee tidak boleh kosong", Toast.LENGTH_SHORT).show()
                closePopUp()
            } else if (!feeEditText.text.isDigitsOnly()) {
                Toast.makeText(this, "Fee hanya boleh angka", Toast.LENGTH_SHORT).show()
                closePopUp()
            } else {
                val response: JSONObject
                if (intent.getSerializableExtra("mobile").toString().toBoolean()) {
                    response = DataController.PostFinal(
                        responseIntent["Username"].toString(),
                        responseIntent["IdLogin"].toString(),
                        responseIntent["NoHP"].toString(),
                        responseIntent["Kode"].toString(),
                        responseIntent["Nominal"].toString(),
                        responseIntent["SaldoAwal"].toString(),
                        feeEditText.text.toString(),
                        responseIntent["Harga"].toString(),
                        responseIntent["SisaSaldo"].toString()
                    ).execute().get()
                } else {
                    response = DepositController.PostFinal(
                        responseIntent["Username"].toString(),
                        responseIntent["IdLogin"].toString(),
                        responseIntent["NoHP"].toString(),
                        responseIntent["Kode"].toString(),
                        responseIntent["Nominal"].toString(),
                        responseIntent["SaldoAwal"].toString(),
                        feeEditText.text.toString(),
                        responseIntent["Harga"].toString(),
                        responseIntent["SisaSaldo"].toString()
                    ).execute().get()
                }

                if (response["Status"].toString() == "0") {
                    closePopUp()
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    finishAndRemoveTask()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    closePopUp()
                }
            }
        }
    }
}
