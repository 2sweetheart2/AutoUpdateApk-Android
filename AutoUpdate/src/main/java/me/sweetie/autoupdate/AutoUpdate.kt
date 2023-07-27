package me.sweetie.autoupdate

import androidx.fragment.app.FragmentManager
import me.sweetie.autoupdate.interfaces.IUpdate
import me.sweetie.autoupdate.objs.UpdateObject
import me.sweetie.autoupdate.requests.HTTPSRequests
import me.sweetie.autoupdate.utils.ShowDialog
import org.json.JSONObject
import kotlin.properties.Delegates

class AutoUpdate(
    private val updateLink: String,
    private val version: Int,
    dialogId: Int?,
    private val jsonParseName: String?
) {

    var dialogId by Delegates.notNull<Int>()

    init {
        if (dialogId == null)
            this.dialogId = R.layout.dialog_auto_update
        else
            this.dialogId = dialogId
    }

    private fun getUpdate(callback: IUpdate) {
        HTTPSRequests.sendPost(updateLink, JSONObject()) {
            callback.getUpdate(jsonParseName?.let { name -> UpdateObject(it.getJSONObject(name)) }
                ?: UpdateObject(it))
        }
    }

    fun getUpdate(fragmentManager: FragmentManager, canSkip: Boolean) {
        getUpdate {
            if (version != it.version) {
                val dialog = ShowDialog(dialogId, it, canSkip)
                dialog.isCancelable = false
                dialog.show(fragmentManager, null)
            }
        }
    }


}