package id.co.agogo.api

import android.os.AsyncTask
import id.co.agogo.R
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HrlController {
    class GET(private val username: String, private val token: String) :
        AsyncTask<Void, Void, JSONArray>() {
        override fun doInBackground(vararg params: Void?): JSONArray {
            try {
                val userAgent = "Mozilla/5.0"
                val url = URL("${ApiController.getUrl()}/hlr.php")
                val httpURLConnection = url.openConnection() as HttpURLConnection

                //add request header
                httpURLConnection.requestMethod = "POST"
                httpURLConnection.setRequestProperty("User-Agent", userAgent)
                httpURLConnection.setRequestProperty("Accept-Language", "en-US,en;q=0.5")
                httpURLConnection.setRequestProperty("Accept", "application/json")

                val body = JSONObject()
                body.put("a", "Hlr")
                body.put("username", username)
                body.put("idlogin", token)

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
                    val response = JSONArray(inputData)
                    val jsonObject = JSONArray()
                    var subJsonObject: JSONObject
                    val arrayList = ArrayList<JSONObject>()
                    var code: String
                    var name: String
                    jsonObject.put(JSONObject("{Status : 1}"))

                    for (value in 0 until response.length()) {
                        code = response.getJSONObject(value)["Hlr"].toString()
                        name = response.getJSONObject(value)["Operator"].toString()
                        if (code != "Default") {
                            subJsonObject = JSONObject()
                            subJsonObject.put("code", code)
                            subJsonObject.put("name", name)

                            arrayList.add(value, subJsonObject)
                        }
                    }
                    var index = 0
                    arrayList.groupBy { it["name"] }.entries.map { (name, group) ->
                        jsonObject.getJSONObject(0)
                            .put(name.toString(), JSONArray(group.toString()))
                        index++
                    }

                    input.close()
                    jsonObject
                } else {
                    JSONArray("[{Status: 1, Pesan: '${R.string.error_Debug}'}, {Status: 1, Pesan: '${R.string.error_Debug}'}]")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return JSONArray("[{Status: 1, Pesan: '${R.string.error_404}'}, {Status: 1, Pesan: '${R.string.error_404}'}]")
            }
        }

    }
}