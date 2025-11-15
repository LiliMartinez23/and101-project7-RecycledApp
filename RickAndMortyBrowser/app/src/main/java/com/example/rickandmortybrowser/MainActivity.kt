package com.example.rickandmortybrowser

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException
import com.example.rickandmortybrowser.model.Character
import com.example.rickandmortybrowser.ui.CharacterAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: CharacterAdapter
    private val client = AsyncHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rv = findViewById<RecyclerView>(R.id.rvCharacters)
        adapter = CharacterAdapter(mutableListOf())
        rv.adapter = adapter
        rv.layoutManager = LinearLayoutManager(this)

        fetchCharacters() // page 1
    }

    private fun fetchCharacters(page: Int = 1) {
        // Rick & Morty characters endpoint (no key required)
        val url = "https://rickandmortyapi.com/api/character?page=$page"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                try {
                    val results = json.jsonObject.getJSONArray("results")
                    val batch = mutableListOf<Character>()
                    for (i in 0 until results.length()) {
                        val obj = results.getJSONObject(i)
                        batch.add(
                            Character(
                                name = obj.getString("name"),
                                species = obj.getString("species"),
                                status = obj.getString("status"),
                                imageUrl = obj.getString("image")
                            )
                        )
                    }
                    adapter.addAll(batch)
                } catch (e: JSONException) {
                    Log.e(TAG, "JSON parse error", e)
                    Toast.makeText(this@MainActivity, "Parse error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Request failed: $statusCode $response", throwable)
                Toast.makeText(this@MainActivity, "Network error: $statusCode", Toast.LENGTH_SHORT).show()
            }
        })
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
