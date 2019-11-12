package id.co.agogo.ppob.dana

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import id.co.agogo.R
import id.co.agogo.api.ProductController
import id.co.agogo.api.ppob.DataController
import id.co.agogo.model.Session
import id.co.agogo.ppob.payment.DepositActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

/**
 * @property product JSONArray
 * @property productNameDANA ArrayList<String>
 * @property productCodeDANA ArrayList<String>
 * @property sessionUser String
 * @property username String
 * @property phoneNumber String
 * @property phoneOperator String
 * @property phoneType String
 * @property progressBar ProgressBar
 * @property phoneTarget EditText
 */
class DanaActivity : AppCompatActivity() {

    private lateinit var product: JSONArray
    private var productNameDANA = ArrayList<String>()
    private var productCodeDANA = ArrayList<String>()
    private var sessionUser = ""
    private var username = "081211610807"
    private var phoneNumber = "081211610807"
    private var phoneOperator = "DANA"
    private var phoneType = "PENUMPANG"
    private lateinit var progressBar: ProgressBar
    private lateinit var phoneTarget: EditText

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

    /**
     * @param savedInstanceState Bundle?
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        sessionUser = Session(this).getString("token").toString()
        username = Session(this).getString("username").toString()
        phoneNumber = Session(this).getString("phone").toString()

        phoneTarget = findViewById(R.id.phoneTargetEditText)
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

        Timer().schedule(2000) {
            try {
                product = ProductController.GET(username, sessionUser).execute().get()
                if (product.getJSONObject(0).length() <= 2) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            getString(R.string.error_404),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        finishAndRemoveTask()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.error_404),
                        Toast.LENGTH_LONG
                    ).show()
                    finishAndRemoveTask()
                }
            }
            for (i in 0 until product.getJSONObject(0).length()) {
                when {
                    product.getJSONObject(0).names().get(i) == "DANA" -> {
                        for (j in 0 until product.getJSONObject(0).getJSONArray("DANA").length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray("DANA").getJSONObject(j)
                            println(value)
                            productCodeDANA.add(value["code"].toString())
                            productNameDANA.add(value["name"].toString())
                        }
                    }
                }
            }

            runOnUiThread {
                closePopUp()

                when (phoneOperator) {
                    "DANA" -> for (i in 0 until productCodeDANA.size) {
                        productSelection(content, content1, optionRow)
                    }
                }
            }
        }
    }

    /**
     * @param layoutParams LayoutParams
     * @param value String
     * @param code String
     * @return View
     */
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
                                val goTo = Intent(
                                    applicationContext,
                                    DepositActivity::class.java
                                ).putExtra("response", response.toString()).putExtra("mobile", true)
                                startActivity(goTo)
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

    /**
     * @param content LinearLayout
     * @param content1 LinearLayout
     * @param optionRow LayoutParams
     */
    private fun productSelection(
        content: LinearLayout,
        content1: LinearLayout,
        optionRow: LinearLayout.LayoutParams
    ) {
        content.removeAllViews()
        content1.removeAllViews()
        var loop = 2
        for (i in 0 until productNameDANA.size) {
            if (i == (loop - 1)) {
                runOnUiThread {
                    content1.addView(
                        generateButton(
                            optionRow,
                            productNameDANA[i],
                            productCodeDANA[i]
                        )
                    )
                    loop += 2
                }
            } else {
                runOnUiThread {
                    content.addView(
                        generateButton(
                            optionRow,
                            productNameDANA[i],
                            productCodeDANA[i]
                        )
                    )
                }
            }
        }
    }

    /**
     * @param code String
     * @return JSONObject
     */
    private fun getResponseDeposit(code: String): JSONObject {
        return DataController.PostDeposit(
            username,
            sessionUser,
            phoneTarget.text.toString(),
            code,
            phoneType
        ).execute().get()
    }

    /**
     * @param value String
     * @return Boolean
     */
    private fun validateNumber(value: String): Boolean {
        return if (value.isEmpty()) {
            false
        } else value.isDigitsOnly()
    }
}