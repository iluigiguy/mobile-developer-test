package com.pbarbieri.mobiledevelopertest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.RecyclerView
import com.pbarbieri.mobiledevelopertest.R
import com.pbarbieri.mobiledevelopertest.models.Story

class RecyclerViewAdapter(private val dataSet: ArrayList<Story>, private val context: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTV: TextView = view.findViewById(R.id.story_title)
        val authorTV: TextView = view.findViewById(R.id.story_author)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.article, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataSet[position].getTitle() !== "null"){
            viewHolder.titleTV.text = dataSet[position].getTitle()
        }else{
            viewHolder.titleTV.text = context.resources.getString(R.string.no_title)
        }

        viewHolder.authorTV.text = dataSet[position].getAuthor()
        viewHolder.itemView.setOnClickListener {
            if(dataSet[position].getUrl() != "null"){
                val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
                val customTabsIntent: CustomTabsIntent = builder.build()
                customTabsIntent.launchUrl(context, android.net.Uri.parse(dataSet[position].getUrl()))
            }else{
                val toast = Toast.makeText(context, context.resources.getString(R.string.no_url), Toast.LENGTH_LONG)
                toast.show()
            }

        }
    }

    override fun getItemCount() = dataSet.size
}