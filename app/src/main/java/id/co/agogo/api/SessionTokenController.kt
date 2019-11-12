package id.co.agogo.api

import android.os.AsyncTask
import id.co.agogo.R
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SessionTokenController {
    class Login(private val username: String, private val password: String) :
        AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            try {
                val userAgent = "Mozilla/5.0"
                val url = URL("${ApiController.getUrl()}/login.php")
                val httpURLConnection = url.openConnection() as HttpURLConnection

                //add request header
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("User-Agent", userAgent)
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                httpURLConnection.setRequestProperty("Accept", "application/json")

                //set body
                val body = JSONObject()
                body.put("a", "ReqLogin")
                body.put("username", username)
                body.put("password", password)

                println(DataIInjectorController().jsonObjectToUrlEndCode(body))

                // Send post request
                httpURLConnection.doOutput = true
                httpURLConnection.doInput = true
                httpURLConnection.useCaches = false
                val write = DataOutputStream(httpURLConnection.outputStream)
                write.writeBytes(DataIInjectorController().jsonObjectToUrlEndCode(body))
                write.flush()
                write.close()

                //get response code
                val responseCode = httpURLConnection.responseCode
                return if (responseCode == 200) {
                    //radder response
                    val input = BufferedReader(
                        InputStreamReader(httpURLConnection.inputStream)
                    )

                    val inputData: String = input.readLine()
                    val response = JSONObject(inputData)
                    input.close()
                    // return json
                    response
                } else {
                    // return json
                    JSONObject("{Status: 3, message: ${R.string.error_404}}")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // return json
                return JSONObject("{Status: 2, message: ${R.string.error_Debug}}")
            }
        }
    }

    class Verification(private val username: String, private val code: String) :
        AsyncTask<Void, Void, JSONObject>() {
        override fun doInBackground(vararg params: Void?): JSONObject {
            try {
                val userAgent = "Mozilla/5.0"
                val url = URL("${ApiController.getUrl()}/login.php")
                val httpURLConnection = url.openConnection() as HttpURLConnection

                //add request header
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("User-Agent", userAgent)
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                httpURLConnection.setRequestProperty("Accept", "application/json")

                //set body
                val body = JSONObject()
                body.put("a", "FinalLogin")
                body.put("username", username)
                body.put("kode", code)

                println(DataIInjectorController().jsonObjectToUrlEndCode(body))

                // Send post request
                httpURLConnection.doOutput = true
                httpURLConnection.doInput = true
                httpURLConnection.useCaches = false
                val write = DataOutputStream(httpURLConnection.outputStream)
                write.writeBytes(DataIInjectorController().jsonObjectToUrlEndCode(body))
                write.flush()
                write.close()

                //get response code
                val responseCode = httpURLConnection.responseCode
                return if (responseCode == 200) {
                    //radder response
                    val input = BufferedReader(
                        InputStreamReader(httpURLConnection.inputStream)
                    )

                    val inputData: String = input.readLine()
                    val response = JSONObject(inputData)
                    input.close()
                    // return json
                    response
                } else {
                    // return json
                    JSONObject("{Status: 3, message: ${R.string.error_404}}")
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // return json
                return JSONObject("{Status: 2, message: ${R.string.error_Debug}}")
            }
        }

    }
}