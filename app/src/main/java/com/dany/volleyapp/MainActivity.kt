package com.dany.volleyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    var queue: RequestQueue? = null

    // Request JSON object {...}
    val objectJson = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02"

    // Request JSON array: [{...}, {...}]
    val arrayJson = "https://jsonplaceholder.typicode.com/comments"

    // Getting the string
    var myUrl = "https://www.google.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        queue = Volley.newRequestQueue(this)

        getString(myUrl)
        getJsonArray(arrayJson)
        getJsonObject(objectJson)
    }

    fun getJsonObject(url: String) {

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        // Get the features JSONArray:
                        val featuresArray = response.getJSONArray("features")

                        for(i in 0..featuresArray.length() -1){
                            val propertyObj = featuresArray.getJSONObject(i).getJSONObject("properties")
                            var place = propertyObj.getString("place")
                            Log.d("===>Place: ", place)

                            // Geometry object
                            val geometryObj = featuresArray.getJSONObject(i).getJSONObject("geometry")

                            var coordinates = geometryObj.getString("coordinates")
                            Log.d("===>Coordinates: ", coordinates)

                            // Printing only the first coordinate index:

                            val coordJsonArray = geometryObj.getJSONArray("coordinates")
                            val firstCoord = coordJsonArray.getDouble(0)

                            Log.d("===>First coordinate: ", firstCoord.toString())
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error -> "Error" })

        queue?.add(jsonObjectRequest)
    }

    fun getJsonArray(url: String) {

        val jsonArray = JsonArrayRequest(Request.Method.GET, url, null,
                // Get the response as JSONArray:
                Response.Listener { response: JSONArray ->
                    try {
                        Log.d("Json Array Response: ", response.toString())

                        // Loop through the array:
                        for (i in 0..response.length()) {
                            // Getting the Json Object inside the array:
                            val movieObj = response.getJSONObject(i)
                            val email = movieObj.getString("email")
                            val id = movieObj.getString("id")

                            Log.d("Email====>", email + ", id: " + id)
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    try {
                        Log.d("===>Error JSONArray: ", error.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                })

        queue?.add(jsonArray)
    }

    fun getString(URL: String) {
        val stringRequest = StringRequest(Request.Method.GET, URL,
                Response.Listener<String> { response ->
                    try {
                        Log.d("===>Response (GET): ", response)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error: VolleyError ->
                    try {
                        Log.d("===>Error", error.toString())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                })

        // Adding the volley request
        queue?.add(stringRequest)
    }
}