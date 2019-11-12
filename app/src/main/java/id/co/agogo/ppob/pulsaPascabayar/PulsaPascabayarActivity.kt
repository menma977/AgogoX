package id.co.agogo.ppob.pulsaPascabayar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.text.isDigitsOnly
import id.co.agogo.R
import id.co.agogo.api.ppob.PaymentController
import id.co.agogo.model.Session
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PulsaPascabayarActivity : AppCompatActivity() {

    private var productName = ArrayList<String>()
    private var productCode = ArrayList<String>()
    private var sessionUser = ""
    private var username = "081211610807"
    private var phoneNumber = "081211610807"
    private var balance = 0
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

    private fun closePopUp() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = ProgressBar.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        sessionUser = Session(this).getString("token").toString()
        username = Session(this).getString("username").toString()
        phoneNumber = Session(this).getString("phone").toString()

        productName.add("TELKOMSEL HALO")
        productName.add("XL (XPLOR)")
        productName.add("INDOSAT (MATRIX)")
        productName.add("THREE (POSTPAID)")
        productName.add("SMARTFREN (POSTPAID)")
        productName.add("ESIA (POSTPAID)")
        productName.add("FREN, MOBI (POSTPAID)")

        productCode.add("HPTSEL;2")
        productCode.add("HPXL;2")
        productCode.add("HPMTRIX;2")
        productCode.add("HPTHREE;2")
        productCode.add("HPSMART;2")
        productCode.add("HPESIA;2")
        productCode.add("HPFREN;2")

        phoneTarget = findViewById(R.id.phoneTargetEditText)
        idUser = findViewById(R.id.tokenNumberEditText)
        val content: LinearLayout = findViewById(R.id.content)
        val content1: LinearLayout = findViewById(R.id.content1)
        progressBar = findViewById(R.id.progressBarPulsa)

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

        for (i in 0 until productName.size) {
            productSelection(content, content1, optionRow)
        }

        closePopUp()
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
                closePopUp()
                Toast.makeText(
                    applicationContext,
                    getString(R.string.fillter_phone_number),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!validateNumber(idUser.text.toString())) {
                closePopUp()
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
                                closePopUp()
//                                val goTo = Intent(
//                                    applicationContext,
//                                    TokenDepositActivity::class.java
//                                ).putExtra("response", response.toString())
//                                startActivity(goTo)
                                finishAndRemoveTask()
                            }
                        }
                        response["Status"].toString() == "1" -> {
                            runOnUiThread {
                                closePopUp()
                                Toast.makeText(
                                    applicationContext,
                                    response["Pesan"].toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        response["Status"].toString() == "2" -> {
                            runOnUiThread {
                                closePopUp()
                                Toast.makeText(
                                    applicationContext,
                                    getString(response["message"].toString().toInt()),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else -> {
                            runOnUiThread {
                                closePopUp()
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
        for (i in 0 until productName.size) {
            if (i == (loop - 1)) {
                runOnUiThread {
                    content1.addView(
                        generateButton(
                            optionRow,
                            productName[i],
                            productCode[i]
                        )
                    )
                    loop += 2
                }
            } else {
                runOnUiThread {
                    content.addView(
                        generateButton(
                            optionRow,
                            productName[i],
                            productCode[i]
                        )
                    )
                }
            }
        }
    }

    private fun getResponseDeposit(code: String): JSONObject {
        return PaymentController.PostDeposit(
            username,
            sessionUser,
            phoneTarget.text.toString(),
            code,
            idUser.text.toString(),
            balance.toString()
        ).execute().get()
    }

    private fun validateNumber(value: String): Boolean {
        return if (value.isEmpty()) {
            false
        } else value.isDigitsOnly()
    }
}
