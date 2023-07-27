package me.sweetie.autoupdate.utils

import android.app.ProgressDialog
import android.content.Context
import me.sweetie.autoupdate.objs.UpdateObject

class APKDownloader(val context: Context, val update: UpdateObject) {
    fun downloadAPK() {
        val mProgressDialog = ProgressDialog(context)
        mProgressDialog.setMessage("Download updates")
        mProgressDialog.setIndeterminate(true)
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        mProgressDialog.setCancelable(false)
        val downloadTask = DownloadTask(context, mProgressDialog, update.version.toString())
        downloadTask.execute(update.updateLink)

        mProgressDialog.setOnCancelListener {
            downloadTask.cancel(false)
        }

    }

}