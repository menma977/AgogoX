package id.co.agogo.api

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

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
                httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest")

                //set body
                val body = JSONObject()
                body.put("username", username)
                body.put("password", password)

                // Send post request
                httpURLConnection.doOutput = true
                val write = DataOutputStream(httpURLConnection.outputStream)
                write.writeBytes(body.toString())
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
                    response.put("code", responseCode)
                    input.close()
                    // return json
                    response
                } else {
                    //radder response
                    val input = BufferedReader(
                        InputStreamReader(httpURLConnection.errorStream)
                    )
                    val inputData: String = input.readLine()
                    val response = JSONObject(inputData)
                    response.put("code", responseCode)

                    // return json
                    response
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // return json
                return JSONObject("{code: 404, message: 'internet tidak setabil', type: 'all'}")
            }
        }
    }
}