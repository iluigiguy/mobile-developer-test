package com.pbarbieri.mobiledevelopertest.models

import android.text.format.DateUtils
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class Story(storyObj: JSONObject) {
    private val story_title: String = if(!storyObj.isNull("title")){
        storyObj.getString("title")
    }else{
        storyObj.getString("story_title")
    }
    private val author: String = storyObj.getString("author")
    private val created_at: String = storyObj.getString("created_at")
    private val story_url: String = storyObj.getString("story_url")
    private val objectID: String = storyObj.getString("objectID")

    fun getTitle(): String{
        return story_title
    }

    fun getAuthor(): String{
        return author + " - " + getCreatedAt()
    }

    fun getObjectId(): String {
        return objectID
    }

    private fun getCreatedAt(): String{
        val utc = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val local = SimpleDateFormat("dd-MM-yy hh:mm a")
        utc.timeZone = TimeZone.getTimeZone("UTC")
        local.timeZone = TimeZone.getTimeZone("America/Caracas")

        val parser = SimpleDateFormat("dd-MM-yy hh:mm a")
        val time = local.format(utc.parse(created_at)!!)
        val now = System.currentTimeMillis()
        val ago = DateUtils.getRelativeTimeSpanString(parser.parse(time)!!.time, now, DateUtils.MINUTE_IN_MILLIS)
        return ago as String
    }

    fun getUrl(): String{
        return story_url
    }
}