package id.co.agogo.ppob.pln

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.text.isDigitsOnly
import id.co.agogo.R
import id.co.agogo.api.ProductController
import id.co.agogo.api.ppob.TokenController
import id.co.agogo.ppob.payment.TokenDepositActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PlnActivity : AppCompatActivity() {

    private lateinit var product: JSONArray
    private var productNamePLN = ArrayList<String>()
    private var productCodePLN = ArrayList<String>()
    private var sessionUser = ""
    private var username = "081211610807"
    private var phoneNumber = "081211610807"
    private var phoneOperator = "PLN"
    private lateinit var progressBar: ProgressBar
    private lateinit var phoneTarget: EditText
    private lateinit var idUser: EditText

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
        setContentView(R.layout.activity_token)

        phoneTarget = findViewById(R.id.phoneTargetEditText)
        idUser = findViewById(R.id.tokenNumberEditText)
        val content: LinearLayout = findViewById(R.id.content)
        val content1: LinearLayout = findViewById(R.id.content1)
        progressBar = findViewById(R.id.progressBarPulsa)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        val optionRow = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        optionRow.topMargin = 10
        optionRow.bottomMargin = 10
        optionRow.rightMargin = 5
        optionRow.leftMargin = 5

        openPopUp()

        content.removeAllViews()
        content1.removeAllViews()
        phoneTarget.setText(phoneNumber)

        Timer().schedule(2000) {
            try {
                product = ProductController.GET(phoneNumber, "0").execute().get()
                if (product.getJSONObject(0).length() <= 2) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, R.string.error_404, Toast.LENGTH_LONG)
                            .show()
                        finishAndRemoveTask()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(applicationContext, R.string.error_404, Toast.LENGTH_LONG).show()
                    finishAndRemoveTask()
                }
            }
            println(product.getJSONObject(0).names())
            for (i in 0 until product.getJSONObject(0).length()) {
                when {
                    product.getJSONObject(0).names().get(i) == "PLN" -> {
                        for (j in 0 until product.getJSONObject(0).getJSONArray("PLN").length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray("PLN").getJSONObject(j)
                            println(value)
                            productCodePLN.add(value["code"].toString())
                            productNamePLN.add(
                                numberFormat.format(
                                    (value["code"].toString().replace(
                                        "PLN",
                                        ""
                                    ) + "000").toInt()
                                )
                            )
                        }
                    }
                }
            }

            runOnUiThread {
                closePupUp()

                when (phoneOperator) {
                    "PLN" -> for (i in 0 until productCodePLN.size) {
                        productSelection(content, content1, optionRow)
                    }
                }
            }
        }
    }

    private fun generateButton(
        layoutParams: LinearLayout.LayoutParams,
        value: String,
        code: String
    ): View {
        val button = Button(this)
        button.layoutParams = layoutParams
        button.text = value
        button.elevation = 20F
        button.setTextColor(Color.parseColor("#ff8492"))
        button.setBackgroundResource(R.drawable.button_default)
        button.setOnClickListener {
            openPopUp()

            if (!validateNumber(phoneTarget.text.toString())) {
                closePupUp()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.fillter_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!validateNumber(idUser.text.toString())) {
                closePupUp()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.fillter_id_user),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Timer().schedule(1000) {
                    val response = getResponseDeposit(code)

                    println("===========================================")
                    println(response)
                    println(value)
                    println(code)
                    println("===========================================")

                    when {
                        response["Status"].toString() == "0" -> {
                            runOnUiThread {
                                closePupUp()
                                val goTo = Intent(
                                    applicationContext,
                                    TokenDepositActivity::class.java
                                ).putExtra("response", response.toString())
                                startActivity(goTo)
                                finishAndRemoveTask()
                            }
                        }
                        response["Status"].toString() == "1" -> {
                            runOnUiThread {
                                closePupUp()
                                Toast.makeText(
                                    applicationContext,
                                    response["Pesan"].toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        response["Status"].toString() == "2" -> {
                            runOnUiThread {
                                closePupUp()
                                Toast.makeText(
                                    applicationContext,
                                    getString(response["message"].toString().toInt()),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                closePupUp()
                                Toast.makeText(
                                    applicationContext,
                                    getString(response["message"].toString().toInt()),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
        return button
    }

    private fun productSelection(
        content: LinearLayout,
        content1: LinearLayout,
        optionRow: LinearLayout.LayoutParams
    ) {
        content.removeAllViews()
        content1.removeAllViews()
        var loop = 2
        for (i in 0 until productNamePLN.size) {
            if (i == (loop - 1)) {
                runOnUiThread {
                    content1.addView(
                        generateButton(
                            optionRow,
                            productNamePLN[i],
                            productCodePLN[i]
                        )
                    )
                    loop += 2
                }
            } else {
                runOnUiThread {
                    content.addView(
                        generateButton(
                            optionRow,
                            productNamePLN[i],
                            productCodePLN[i]
                        )
                    )
                }
            }
        }
    }

    private fun getResponseDeposit(code: String): JSONObject {
        return TokenController.PostDeposit(
            username,
            sessionUser,
            phoneTarget.text.toString(),
            idUser.text.toString(),
            code
        ).execute().get()
    }

    private fun validateNumber(value: String): Boolean {
        return if (value.isEmpty()) {
            false
        } else value.isDigitsOnly()
    }
}
