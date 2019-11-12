package id.co.agogo.ppob.pulsa

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.text.isDigitsOnly
import androidx.core.widget.doOnTextChanged
import id.co.agogo.R
import id.co.agogo.api.HrlController
import id.co.agogo.api.ProductController
import id.co.agogo.api.ppob.DepositController
import id.co.agogo.ppob.payment.DepositActivity
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class PulsaActivity : AppCompatActivity() {

    private lateinit var hlr: JSONArray
    private lateinit var product: JSONArray
    private var productNameTELKOMSEL = ArrayList<String>()
    private var productCodeTELKOMSEL = ArrayList<String>()
    private var productNameTELKOMSELDATA = ArrayList<String>()
    private var productCodeTELKOMSELDATA = ArrayList<String>()
    private var productNameINDOSAT = ArrayList<String>()
    private var productCodeINDOSAT = ArrayList<String>()
    private var productNameINDOSATDATA = ArrayList<String>()
    private var productCodeINDOSATDATA = ArrayList<String>()
    private var productNameXL = ArrayList<String>()
    private var productCodeXL = ArrayList<String>()
    private var productNameXLDATA = ArrayList<String>()
    private var productCodeXLDATA = ArrayList<String>()
    private var productNameAXIS = ArrayList<String>()
    private var productCodeAXIS = ArrayList<String>()
    private var productNameAXISDATA = ArrayList<String>()
    private var productCodeAXISDATA = ArrayList<String>()
    private var productNameSMART = ArrayList<String>()
    private var productCodeSMART = ArrayList<String>()
    private var productNameSMARTDATA = ArrayList<String>()
    private var productCodeSMARTDATA = ArrayList<String>()
    private var productNameTHREE = ArrayList<String>()
    private var productCodeTHREE = ArrayList<String>()
    private var productNameTHREEDATA = ArrayList<String>()
    private var productCodeTHREEDATA = ArrayList<String>()
    private var hrlTELKOMSEL = ArrayList<String>()
    private var hrlINDOSAT = ArrayList<String>()
    private var hrlXL = ArrayList<String>()
    private var hrlAXIS = ArrayList<String>()
    private var hrlSMART = ArrayList<String>()
    private var hrlTHREE = ArrayList<String>()
    private var username = "081211610807"
    private var phoneNumber = "081211610807"
    private var phoneOperator = ""
    private var phoneType = "PULSA"
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

    private fun closePupUp() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        progressBar.visibility = ProgressBar.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pulsa)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        phoneTarget = findViewById(R.id.phoneTargetEditText)
        val switchPackage: Switch = findViewById(R.id.switchPackage)
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
                product = ProductController.GET(phoneNumber, "0").execute().get()
                hlr = HrlController.GET(phoneNumber).execute().get()
                if (product.getJSONObject(0).length() <= 2) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, R.string.error_404, Toast.LENGTH_LONG)
                            .show()
                        finishAndRemoveTask()
                    }
                }
                if (hlr.getJSONObject(0).length() <= 2) {
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

            for (i in 0 until product.getJSONObject(0).length()) {
                var operator: String
                when {
                    product.getJSONObject(0).names().get(i) == "TELKOMSEL" -> {
                        operator = "TELKOMSEL"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeTELKOMSEL.add(value["code"].toString())
                                productNameTELKOMSEL.add(value["name"].toString())
                            } else {
                                productCodeTELKOMSELDATA.add(value["code"].toString())
                                productNameTELKOMSELDATA.add(value["name"].toString())
                            }
                        }
                    }
                    product.getJSONObject(0).names().get(i) == "INDOSAT" -> {
                        operator = "INDOSAT"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeINDOSAT.add(value["code"].toString())
                                productNameINDOSAT.add(value["name"].toString())
                            } else {
                                productCodeINDOSATDATA.add(value["code"].toString())
                                productNameINDOSATDATA.add(value["name"].toString())
                            }
                        }
                    }
                    product.getJSONObject(0).names().get(i) == "XL" -> {
                        operator = "XL"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeXL.add(value["code"].toString())
                                productNameXL.add(value["name"].toString())
                            } else {
                                productCodeXLDATA.add(value["code"].toString())
                                productNameXLDATA.add(value["name"].toString())
                            }
                        }
                    }
                    product.getJSONObject(0).names().get(i) == "AXIS" -> {
                        operator = "AXIS"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeAXIS.add(value["code"].toString())
                                productNameAXIS.add(value["name"].toString())
                            } else {
                                productCodeAXISDATA.add(value["code"].toString())
                                productNameAXISDATA.add(value["name"].toString())
                            }
                        }
                    }
                    product.getJSONObject(0).names().get(i) == "SMART" -> {
                        operator = "SMART"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeSMART.add(value["code"].toString())
                                productNameSMART.add(value["name"].toString())
                            } else {
                                productCodeSMARTDATA.add(value["code"].toString())
                                productNameSMARTDATA.add(value["name"].toString())
                            }
                        }
                    }
                    product.getJSONObject(0).names().get(i) == "THREE" -> {
                        operator = "THREE"
                        for (j in 0 until product.getJSONObject(0).getJSONArray(operator).length()) {
                            val value =
                                product.getJSONObject(0).getJSONArray(operator).getJSONObject(j)
                            if (value["typeProduct"].toString() != "DATA") {
                                productCodeTHREE.add(value["code"].toString())
                                productNameTHREE.add(value["name"].toString())
                            } else {
                                productCodeTHREEDATA.add(value["code"].toString())
                                productNameTHREEDATA.add(value["name"].toString())
                            }
                        }
                    }
                }
            }

            for (i in 0 until hlr.getJSONObject(0).length()) {
                when {
                    hlr.getJSONObject(0).names().get(i) == "TELKOMSEL" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("TELKOMSEL").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("TELKOMSEL").getJSONObject(j)
                            hrlTELKOMSEL.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "TELKOMSEL"
                            }
                        }
                    }
                    hlr.getJSONObject(0).names().get(i) == "INDOSAT" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("INDOSAT").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("INDOSAT").getJSONObject(j)
                            hrlINDOSAT.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "INDOSAT"
                            }
                        }
                    }
                    hlr.getJSONObject(0).names().get(i) == "XL" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("XL").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("XL").getJSONObject(j)
                            hrlXL.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "XL"
                            }
                        }
                    }
                    hlr.getJSONObject(0).names().get(i) == "AXIS" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("AXIS").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("AXIS").getJSONObject(j)
                            hrlAXIS.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "AXIS"
                            }
                        }
                    }
                    hlr.getJSONObject(0).names().get(i) == "SMART" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("SMART").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("SMART").getJSONObject(j)
                            hrlSMART.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "SMART"
                            }
                        }
                    }
                    hlr.getJSONObject(0).names().get(i) == "THREE" -> {
                        for (j in 0 until hlr.getJSONObject(0).getJSONArray("THREE").length()) {
                            val value =
                                hlr.getJSONObject(0).getJSONArray("THREE").getJSONObject(j)
                            hrlTHREE.add(value["code"].toString())
                            if (phoneNumber.substring(0, 4) == value["code"].toString()) {
                                phoneOperator = "THREE"
                            }
                        }
                    }
                }
            }

            runOnUiThread {
                closePupUp()

                when (phoneOperator) {
                    "TELKOMSEL" -> for (i in 0 until productCodeTELKOMSEL.size) {
                        productSelection(phoneNumber, content, content1, numberFormat, optionRow)
                    }
                    "INDOSAT" -> for (i in 0 until productCodeINDOSAT.size) {
                        productSelection(phoneNumber, content, content1, numberFormat, optionRow)
                    }
                    "XL" -> for (i in 0 until productCodeXL.size) {
                        productSelection(phoneNumber, content, content1, numberFormat, optionRow)
                    }
                    "SMART" -> for (i in 0 until productCodeSMART.size) {
                        productSelection(phoneNumber, content, content1, numberFormat, optionRow)
                    }
                    "THREE" -> for (i in 0 until productCodeTHREE.size) {
                        productSelection(phoneNumber, content, content1, numberFormat, optionRow)
                    }
                }
            }
        }

        phoneTarget.doOnTextChanged { text, _, _, _ ->
            try {
                when {
                    text.toString().isEmpty() -> {
                        content.removeAllViews()
                        content1.removeAllViews()
                    }
                    text.toString().length == 4 -> {
                        content.removeAllViews()
                        content1.removeAllViews()
                        Timer().schedule(1000) {
                            runOnUiThread {
                                try {
                                    productSelection(
                                        text.toString(),
                                        content,
                                        content1,
                                        numberFormat,
                                        optionRow
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                    else -> {
                        //TODO: Add when phone number is remove
                    }
                }
            } catch (e: Exception) {
                content.removeAllViews()
                content1.removeAllViews()
            }
        }

        switchPackage.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                switchPackage.setText(R.string.package_data)
                phoneType = "DATA"
                content.removeAllViews()
                content1.removeAllViews()
                productSelection(
                    phoneTarget.text.toString(),
                    content,
                    content1,
                    numberFormat,
                    optionRow
                )
            } else {
                switchPackage.setText(R.string.package_pulsa)
                phoneType = "PULSA"
                content.removeAllViews()
                productSelection(
                    phoneTarget.text.toString(),
                    content,
                    content1,
                    numberFormat,
                    optionRow
                )
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
                    "${getString(R.string.phone_number)} tidak boleh kosong dan hanya boleh angka",
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
                                    DepositActivity::class.java
                                ).putExtra("response", response.toString()).putExtra("mobile", false)
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
        text: String,
        content: LinearLayout,
        content1: LinearLayout,
        numberFormat: NumberFormat,
        optionRow: LinearLayout.LayoutParams
    ) {
        content.removeAllViews()
        content1.removeAllViews()
        var loop: Int
        when {
            hrlTELKOMSEL.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> {
                    loop = 2
                    for (i in 0 until productNameTELKOMSELDATA.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        productNameTELKOMSELDATA[i],
                                        productCodeTELKOMSELDATA[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        productNameTELKOMSELDATA[i],
                                        productCodeTELKOMSELDATA[i]
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    loop = 2
                    for (i in 0 until productCodeTELKOMSEL.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeTELKOMSEL[i].replace(
                                                "TELKOMSEL",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeTELKOMSEL[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeTELKOMSEL[i].replace(
                                                "TELKOMSEL",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeTELKOMSEL[i]
                                    )
                                )
                            }
                        }
                    }
                }
            }
            hrlINDOSAT.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> {
                    loop = 2
                    for (i in 0 until productNameINDOSATDATA.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        productNameINDOSATDATA[i],
                                        productCodeINDOSATDATA[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        productNameINDOSATDATA[i],
                                        productCodeINDOSATDATA[i]
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    loop = 2
                    for (i in 0 until productCodeINDOSAT.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeINDOSAT[i].replace(
                                                "INDOSAT",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeINDOSAT[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeINDOSAT[i].replace(
                                                "INDOSAT",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeINDOSAT[i]
                                    )
                                )
                            }
                        }
                    }
                }
            }
            hrlXL.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> {
                    loop = 2
                    for (i in 0 until productNameXLDATA.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        productNameXLDATA[i],
                                        productCodeXLDATA[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        productNameXLDATA[i],
                                        productCodeXLDATA[i]
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    loop = 2
                    for (i in 0 until productCodeXL.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeXL[i].replace(
                                                "XL",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeXL[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeXL[i].replace(
                                                "XL",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeXL[i]
                                    )
                                )
                            }
                        }
                    }
                }
            }
            hrlSMART.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> {
                    loop = 2
                    for (i in 0 until productNameSMARTDATA.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        productNameSMARTDATA[i],
                                        productCodeSMARTDATA[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        productNameSMARTDATA[i],
                                        productCodeSMARTDATA[i]
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    loop = 2
                    for (i in 0 until productCodeSMART.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeSMART[i].replace(
                                                "SMART",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeSMART[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeSMART[i].replace(
                                                "SMART",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeSMART[i]
                                    )
                                )
                            }
                        }
                    }
                }
            }
            hrlTHREE.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> {
                    loop = 2
                    for (i in 0 until productNameTHREEDATA.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        productNameTHREEDATA[i],
                                        productCodeTHREEDATA[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        productNameTHREEDATA[i],
                                        productCodeTHREEDATA[i]
                                    )
                                )
                            }
                        }
                    }
                }
                else -> {
                    loop = 2
                    for (i in 0 until productCodeTHREE.size) {
                        if (i == (loop - 1)) {
                            runOnUiThread {
                                content1.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeTHREE[i].replace(
                                                "THREE",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeTHREE[i]
                                    )
                                )
                                loop += 2
                            }
                        } else {
                            runOnUiThread {
                                content.addView(
                                    generateButton(
                                        optionRow,
                                        numberFormat.format(
                                            "${productCodeTHREE[i].replace(
                                                "THREE",
                                                ""
                                            )}000".toInt()
                                        ),
                                        productCodeTHREE[i]
                                    )
                                )
                            }
                        }
                    }
                }
            }
            else -> {
                runOnUiThread {
                    content.removeAllViews()
                    content1.removeAllViews()
                }
            }
        }
    }

    private fun getResponseDeposit(code: String): JSONObject {
        return when (phoneType) {
            "DATA" -> DepositController.PostDeposit(
                username,
                phoneTarget.text.toString(),
                code,
                "DATA"
            ).execute().get()
            else -> DepositController.PostDeposit(
                username,
                phoneTarget.text.toString(),
                code,
                "PULSA"
            ).execute().get()
        }
    }

    private fun validateNumber(value: String): Boolean {
        return if (value.isEmpty()) {
            false
        } else value.isDigitsOnly()
    }
}
