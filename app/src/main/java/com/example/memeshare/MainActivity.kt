package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var currentImageUrl: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    private fun loadMeme()
    {
        // Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE
        next.isEnabled = false
        share.isEnabled = false
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        // Request a string response from the provided URL.
        val stringRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                currentImageUrl = response.getString("url")

                Glide.with(this).load(currentImageUrl).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        next.isEnabled = true
                        share.isEnabled = true
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.visibility = View.GONE
                        next.isEnabled = true
                        share.isEnabled = true
                        return false
                    }
                }).into(memeImageView)
            },
            {

            })

            // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey check out this cool meme I got from Reddit")
        val chooser = Intent.createChooser(intent, "Share this meme using")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}