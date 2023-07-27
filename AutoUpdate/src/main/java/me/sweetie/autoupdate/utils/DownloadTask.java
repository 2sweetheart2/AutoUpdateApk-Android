package me.sweetie.autoupdate.utils;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class DownloadTask extends AsyncTask<String, Integer, String> {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private PowerManager.WakeLock mWakeLock;
    private final ProgressDialog mProgressDialog;
    private final String v;
    private File file;

    public DownloadTask(Context context, ProgressDialog dialog, String version) {
        this.context = context;
        this.mProgressDialog = dialog;
        this.v = version;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            int fileLength = connection.getContentLength();
            input = connection.getInputStream();
            System.out.println(context.getExternalCacheDir().getPath());

            File path = context.getExternalCacheDir();
            File file = new File(path, "update_" + v + ".apk");
            System.out.println(file.getPath());
            this.file = file;
            try {
                path.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (file.exists())
                file.delete();

            output = new FileOutputStream(file.getPath());
            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                if (fileLength > 0)
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();

            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire();
        mProgressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgress(progress[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        mProgressDialog.dismiss();
        System.out.println(result);
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else {
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
            try {
                openAPK();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void openAPK() throws IOException {
        Intent install = new Intent(Intent.ACTION_INSTALL_PACKAGE);
        System.out.println(Uri.fromFile(file).toString());
        System.out.println(context.getPackageName());
        install.setDataAndType(FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file),
                "application/vnd.android.package-archive");
        install.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(install);
    }

}