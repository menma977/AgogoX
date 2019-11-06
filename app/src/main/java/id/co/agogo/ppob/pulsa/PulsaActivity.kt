package id.co.agogo.ppob.pulsa

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import id.co.agogo.R
import id.co.agogo.api.HrlController
import id.co.agogo.api.ProductController
import org.json.JSONArray
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
    private var phoneNumber = "0812000000"
    private var phoneOperator = ""
    private var phoneType = "REGULER"
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pulsa)

        val idr = Locale("in", "ID")
        val numberFormat = NumberFormat.getCurrencyInstance(idr)

        val phoneTarget: EditText = findViewById(R.id.phoneTargetEditText)
        val content: LinearLayout = findViewById(R.id.content)
        progressBar = findViewById(R.id.progressBarPulsa)

        val optionRow = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            0f
        )
        optionRow.topMargin = 10
        optionRow.bottomMargin = 10

        progressBar.visibility = ProgressBar.VISIBLE
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        content.removeAllViews()
        phoneTarget.setText(phoneNumber)

        Timer().schedule(2000) {
            product = ProductController.GET(phoneNumber, "0").execute().get()
            hlr = HrlController.GET(phoneNumber).execute().get()

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
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                progressBar.visibility = ProgressBar.GONE

                when (phoneOperator) {
                    "TELKOMSEL" -> for (i in 0 until productCodeTELKOMSEL.size) {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTELKOMSEL[i].replace(
                                        "TELKOMSEL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                    "INDOSAT" -> for (i in 0 until productCodeINDOSAT.size) {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeINDOSAT[i].replace(
                                        "INDOSAT",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                    "XL" -> for (i in 0 until productCodeXL.size) {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeXL[i].replace(
                                        "XL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                    "SMART" -> for (i in 0 until productCodeSMART.size) {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeSMART[i].replace(
                                        "SMART",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                    "THREE" -> for (i in 0 until productCodeTHREE.size) {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTHREE[i].replace(
                                        "THREE",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
        }

        phoneTarget.doOnTextChanged { text, _, _, _ ->
            try {
                when {
                    text.toString().isEmpty() -> {
                        content.removeAllViews()
                    }
                    text.toString().length == 4 -> {
                        content.removeAllViews()
                        Timer().schedule(1000) {
                            runOnUiThread {
                                try {
                                    productSelection(
                                        text.toString(),
                                        content,
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
                        //content.removeAllViews()
                    }
                }
            } catch (e: Exception) {
                content.removeAllViews()
            }
        }
    }

    private fun generateButton(layoutParams: LinearLayout.LayoutParams, value: String): View {
        val button = Button(this)
        button.layoutParams = layoutParams
        button.text = value
        button.elevation = 20F
        button.setTextColor(Color.parseColor("#ff8492"))
        button.setBackgroundResource(R.drawable.button_default)
        return button
    }

    private fun productSelection(
        text: String,
        content: LinearLayout,
        numberFormat: NumberFormat,
        optionRow: LinearLayout.LayoutParams
    ) {
        when {
            hrlTELKOMSEL.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> for (i in 0 until productCodeTELKOMSELDATA.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTELKOMSELDATA[i].replace(
                                        "TELKOMSEL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
                else -> for (i in 0 until productCodeTELKOMSEL.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTELKOMSEL[i].replace(
                                        "TELKOMSEL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
            hrlINDOSAT.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> for (i in 0 until productCodeINDOSATDATA.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeINDOSATDATA[i].replace(
                                        "TELKOMSEL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
                else -> for (i in 0 until productCodeINDOSAT.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeINDOSAT[i].replace(
                                        "TELKOMSEL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
            hrlXL.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> for (i in 0 until productCodeXLDATA.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeXL[i].replace(
                                        "XL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
                else -> for (i in 0 until productCodeXL.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeXL[i].replace(
                                        "XL",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
            hrlSMART.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> for (i in 0 until productCodeSMARTDATA.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeSMARTDATA[i].replace(
                                        "SMART",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
                else -> for (i in 0 until productCodeSMART.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeSMART[i].replace(
                                        "SMART",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
            hrlTHREE.any { target -> text.contains(target) } -> when (phoneType) {
                "DATA" -> for (i in 0 until productCodeTHREEDATA.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTHREEDATA[i].replace(
                                        "THREE",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
                else -> for (i in 0 until productCodeTHREE.size) {
                    runOnUiThread {
                        content.addView(
                            generateButton(
                                optionRow,
                                numberFormat.format(
                                    "${productCodeTHREE[i].replace(
                                        "THREE",
                                        ""
                                    )}000".toInt()
                                )
                            )
                        )
                    }
                }
            }
            else -> {
                runOnUiThread {
                    content.removeAllViews()
                }
            }
        }
    }
}
