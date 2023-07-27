package me.sweetie.autoupdate.objs

import org.json.JSONObject

class UpdateTitle(jsonObject: JSONObject) {
    val title:String
    val text:String
    init {
        title = jsonObject.getString("title")
        text =jsonObject.getString("text")
    }
}