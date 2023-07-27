package me.sweetie.autoupdate.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import me.sweetie.autoupdate.R
import me.sweetie.autoupdate.objs.UpdateObject

class ShowDialog(
    private val layoutId: Int,
    private val update: UpdateObject,
    val canSkip: Boolean
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;

            val layout = inflater.inflate(layoutId, null)

            val list: LinearLayout = layout.findViewById(R.id.update_update_change_log_list)
            layout.findViewById<TextView>(R.id.update_update_title).text = update.title
            layout.findViewById<TextView>(R.id.update_update_version).text = update.versionString
            for (updateLog in update.changeLogsText) {
                val updateContent = inflater.inflate(R.layout.list_content_updater, null)
                updateContent.findViewById<TextView>(R.id.change_log_text_title).text =
                    updateLog.title
                updateContent.findViewById<TextView>(R.id.change_log_text_text).text =
                    updateLog.text
                list.addView(updateContent)
            }
            builder.setCancelable(false)
            if (!canSkip) {
                layout.findViewById<Button>(R.id.update_update_skip_btn).isEnabled = false
                layout.findViewById<Button>(R.id.update_update_skip_btn).isActivated = false
            }

            layout.findViewById<Button>(R.id.update_update_skip_btn).setOnClickListener { dismiss() }
            layout.findViewById<Button>(R.id.update_update_update_btn).setOnClickListener {
                println(update.updateLink)
                APKDownloader(requireContext(), update).downloadAPK()
            }
            builder.setView(layout)
            return builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}