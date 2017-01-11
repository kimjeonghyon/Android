package com.john.android.parkinginfo;

import android.os.AsyncTask;
import android.util.Log;
import android.util.StringBuilderPrinter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by jeonghyonkim on 2016. 10. 10..
 */
public class DownloadHtmlTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        StringBuilder html = new StringBuilder();
        OutputStreamWriter out;
        try {

            URL url = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

//            Log.d("Task",url.toString());

            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

//                Log.d("Task","conn != null");

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));
                    for (; ; ) {
                        String line = br.readLine();
                        if (line == null) break;
                        html.append(line + "\n");
                    }
                    br.close();
                }

                conn.disconnect();
            }


        } catch (Exception e) {
            Log.d("Task","Exception:" + e.getMessage());
        }
        return html.toString();
    }
}
