package id.co.agogo.ppob.payment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.core.text.isDigitsOnly
import id.co.agogo.R
import id.co.agogo.api.ppob.TokenController
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class TokenDepositActivity : AppCompatActivity() {

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

    private fun closePupUp() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = ProgressBar.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token_deposit)

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
        println(responseIntent)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        try {
            phoneNumber.text = responseIntent["NoHP"].toString()
            code.text = responseIntent["IdPel"].toString()
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

        closePupUp()

        feeEditText.requestFocus()

        finish.setOnClickListener {
            openPopUp()
            if (feeEditText.text.isEmpty()) {
                Toast.makeText(this, "Fee tidak boleh kosong", Toast.LENGTH_SHORT).show()
                closePupUp()
            } else if (!feeEditText.text.isDigitsOnly()) {
                Toast.makeText(this, "Fee hanya boleh angka", Toast.LENGTH_SHORT).show()
                closePupUp()
            } else {
                val response: JSONObject = TokenController.PostFinal(
                    responseIntent["Username"].toString(),
                    responseIntent["IdLogin"].toString(),
                    responseIntent["NoHP"].toString(),
                    responseIntent["Kode"].toString(),
                    responseIntent["IdPel"].toString(),
                    responseIntent["SaldoAwal"].toString(),
                    feeEditText.text.toString(),
                    responseIntent["Harga"].toString(),
                    responseIntent["SisaSaldo"].toString()
                ).execute().get()

                if (response["Status"].toString() == "0") {
                    closePupUp()
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    finishAndRemoveTask()
                } else {
                    Toast.makeText(this, response["Pesan"].toString(), Toast.LENGTH_LONG).show()
                    closePupUp()
                }
            }
        }
    }
}
