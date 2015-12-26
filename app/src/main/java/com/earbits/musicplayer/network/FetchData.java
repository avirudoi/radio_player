package com.earbits.musicplayer.network;

import android.content.Context;
import android.util.Log;

import com.earbits.musicplayer.listener.ApiCallListener;
import com.earbits.musicplayer.model.SongObject;
import com.google.gson.Gson;

import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by avirudoi on 12/24/15.
 */
public class FetchData {

    HttpURLConnection urlConnection;
    URL urlObj;
    StringBuilder result;
    BufferedReader reader = null;
    private WeakReference<ApiCallListener> mApiListener = new WeakReference<>(null);

    final static String URL = "http://streaming.earbits.com/api/v1/track.json?stream_id=5654d7c3c5aa6e00030021aa";
    /**
     * Constructor to initiate the web service call
     * @param context
     */
    public  void fetchSongData(Context context){

        try {
            urlObj = new URL(URL);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            urlConnection.connect();

            InputStream stream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            processApiResponse(urlConnection.getResponseCode(),result.toString());
            }catch (IOException e){
                e.printStackTrace();
            }finally {
            //very important to check for not null here
            if(urlConnection !=null){
                urlConnection.disconnect();
            }
        }
    }
    /**
     * Helper method to trigger the listeners with the appropriate data
     * @param statusCode
     * @param response
     */
    private void processApiResponse (int statusCode, String response) {
        if (getApiListener() != null) {
            if (statusCode == 200 || statusCode == 201) {
                Gson json = new Gson();
                getApiListener().onSuccess(json.fromJson(response, SongObject.class));
            } else if(statusCode == -1) {
                getApiListener().onFailure("Default failure message");
            } else {
                getApiListener().onFailure("Get Failure message from response");
            }
        }
    }

    public void  setApiCallListener(ApiCallListener listener){
        mApiListener = new WeakReference<>(listener);
    }

    public ApiCallListener getApiListener() {
        return mApiListener.get();
    }
}
