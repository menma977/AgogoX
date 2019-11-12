package id.co.agogo.api.ppob

import android.os.AsyncTask
import id.co.agogo.R
import id.co.agogo.api.ApiController
import id.co.agogo.api.DataIInjectorController
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class TokenController {
    class PostDeposit(
        private val username: String,
        private val sessionCode: String,
        private val phone: String,
        private val idUser: String,
        private val type: String
    ) : AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            try {
                val userAgent = "Mozilla/5.0"
                val url = URL("${ApiController.getUrl()}/isitoken.php")
                val httpURLConnection = url.openConnection() as HttpsURLConnection

                //add request header
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("User-Agent", userAgent)
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                httpURLConnection.setRequestProperty("Accept", "application/json")

                //set body
                val body = JSONObject()
                body.put("a", "ReqToken")
                body.put("username", username)
                body.put("idlogin", sessionCode)
                body.put("nohp", phone)
                body.put("idpel", idUser)
                body.put("type", type)

                println(DataIInjectorController().jsonObjectToUrlEndCode(body))

                // Send post request
                httpURLConnection.doOutput = true
                val write = DataOutputStream(httpURLConnection.outputStream)
                write.writeBytes(DataIInjectorController().jsonObjectToUrlEndCode(body))
                write.flush()
                write.close()

                val responseCode = httpURLConnection.responseCode
                return if (responseCode == 200) {
                    val input = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                    val inputData: String = input.readLine()
                    val response = JSONObject(inputData)
                    input.close()
                    response
                } else {
                    JSONObject("{Status: 3, message: ${R.string.error_404}}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return JSONObject("{Status: 2, message: ${R.string.error_Debug}}")
            }
        }
    }

    class PostFinal(
        private val username: String,
        private val sessionCode: String,
        private val phone: String,
        private val payCode: String,
        private val idUser: String,
        private val firstBalance: String,
        private val markupAdmin: String,
        private val price: String,
        private val remainingBalance: String
    ) : AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            try {
                val userAgent = "Mozilla/5.0"
                val url = URL("${ApiController.getUrl()}/isitoken.php")
                val httpURLConnection = url.openConnection() as HttpsURLConnection

                //add request header
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("User-Agent", userAgent)
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                httpURLConnection.setRequestProperty("Accept", "application/json")

                //set body
                val body = JSONObject()
                body.put("a", "PayToken")
                body.put("username", username)
                body.put("idlogin", sessionCode)
                body.put("nohp", phone)
                body.put("kode", payCode)
                body.put("idpel", idUser)
                body.put("saldoawal", firstBalance)
                body.put("markup", markupAdmin)
                body.put("harga", price)
                body.put("sisasaldo", remainingBalance)

                println(DataIInjectorController().jsonObjectToUrlEndCode(body))

                // Send post request
                httpURLConnection.doOutput = true
                val write = DataOutputStream(httpURLConnection.outputStream)
                write.writeBytes(DataIInjectorController().jsonObjectToUrlEndCode(body))
                write.flush()
                write.close()

                val responseCode = httpURLConnection.responseCode
                return if (responseCode == 200) {
                    val input = BufferedReader(
                        InputStreamReader(httpURLConnection.inputStream)
                    )

                    val inputData: String = input.readLine()
                    val response = JSONObject(inputData)
                    input.close()
                    response
                } else {
                    JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return JSONObject("{Status: 1, Pesan: 'internet tidak setabil'}")
            }
        }
    }
}