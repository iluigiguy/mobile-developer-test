package com.pbarbieri.mobiledevelopertest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.pbarbieri.mobiledevelopertest.adapters.RecyclerViewAdapter
import com.pbarbieri.mobiledevelopertest.adapters.SwipeToDeleteCallback
import com.pbarbieri.mobiledevelopertest.models.Story
import org.json.JSONArray


class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var stories: RecyclerView
    private lateinit var storiesArr: ArrayList<Story>
    private lateinit var adapter: RecyclerViewAdapter
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        stories = findViewById(R.id.stories)
        getStories()
        swipeContainer = findViewById(R.id.swipe_container)
        swipeContainer.setOnRefreshListener(this)
        swipeContainer.isRefreshing = true
    }

    private fun getStories(){
        val queue = Volley.newRequestQueue(this)
        val url = getString(R.string.api_url)

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val objects = response.getJSONArray("hits")
                setArrayList(objects)
            },
            {
                val savedStories = JSONArray(sharedPref.getString(getString(R.string.offline_stories),"[]"))
                if(savedStories.length() > 0){
                    setArrayList(savedStories)
                }else{
                    swipeContainer.isRefreshing = false
                }
            })

        queue.add(jsonRequest)
    }

    private fun initSwipeHandler(){
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(
            this,
            getString(R.string.delete)
        ) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                addToBlacklist(storiesArr[position].getObjectId())
                storiesArr.removeAt(position)
                adapter.notifyItemRemoved(position)
                saveStories(storiesArr)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(stories)
    }

    override fun onRefresh() {
        getStories()
    }

    private fun saveStories(array: ArrayList<Story>){
        val gson = Gson()
        val json = gson.toJson(array)
        with(sharedPref.edit()) {
            putString(getString(R.string.offline_stories), json)
            apply()
        }
    }

    private fun setArrayList(jsonArray: JSONArray){
        var blacklistString = getBlacklist()
        var array = JSONArray(blacklistString)
        storiesArr = ArrayList()
        for (i in 0 until jsonArray.length()) {
            if(!array.toString().contains(jsonArray.getJSONObject(i).getString("objectID"))){
                storiesArr.add(Story(jsonArray.getJSONObject(i)))
            }
        }

        adapter = RecyclerViewAdapter(storiesArr, this)
        stories.adapter = adapter
        stories.layoutManager = LinearLayoutManager(this)

        initSwipeHandler()
        swipeContainer.isRefreshing = false
        saveStories(storiesArr)
    }

    private fun addToBlacklist(objectID: String){
        var blacklist = JSONArray(getBlacklist())
        blacklist.put(objectID)

        with(sharedPref.edit()) {
            putString(getString(R.string.blacklist), blacklist.toString())
            apply()
        }
    }

    private fun getBlacklist(): String{
        return sharedPref.getString(getString(R.string.blacklist), "[]").toString()
    }

}
