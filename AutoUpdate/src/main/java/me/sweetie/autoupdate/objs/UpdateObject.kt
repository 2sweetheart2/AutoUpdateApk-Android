package me.sweetie.autoupdate.objs

import org.json.JSONObject

class UpdateObject(jsonObject: JSONObject) {
    var changeLogsText:ArrayList<UpdateTitle> = ArrayList()
    val updateLink:String
    val version:Int
    val title:String
    val versionString:String

    init {
        updateLink = jsonObject.getString("update_link")
        version = jsonObject.getInt("version")
        versionString = jsonObject.getString("version_str")
        title = jsonObject.getString("title")
        val ar = jsonObject.getJSONArray("change_log")
        for(i in 0 until ar.length()){
            changeLogsText.add(UpdateTitle(ar.getJSONObject(i)))
        }
    }
}