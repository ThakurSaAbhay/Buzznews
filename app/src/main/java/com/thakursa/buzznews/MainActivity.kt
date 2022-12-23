package com.thakursa.buzznews

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.Response.Listener
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {

    private lateinit var mAdapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerview.layoutManager=LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerview.adapter=mAdapter
    }
    private fun fetchData(){
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/general/in.json"
        val jsonObjectRequest =JsonObjectRequest(

            Request.Method.GET,
            url,
            null,
            Listener {
                val newsJSONArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for (i in 0 until newsJSONArray.length()){
                    val newsJSONObject=newsJSONArray.getJSONObject(i)
                    val news=News(
                        newsJSONObject.getString("title"),
                        newsJSONObject.getString("author"),
                        newsJSONObject.getString("url"),
                        newsJSONObject.getString("urlToImage"),
                        )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {

            }


        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder =  CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}