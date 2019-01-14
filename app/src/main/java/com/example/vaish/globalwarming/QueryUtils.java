package com.example.vaish.globalwarming;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vaish on 23-09-2016.
 */
public final class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils(){

    }

    public static List<GlobalWarming> fetchData(String requestUrl){
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHTTPrequest(url);

        }catch (IOException e){
            Log.e(LOG_TAG,"Problem in making the HTTP request",e);
        }
        List<GlobalWarming> globalWarmings = extractFeaturesFromJson(jsonResponse);
        Log.i("QueryUtils"," Data fetched");
        return globalWarmings;
     }

    private static String makeHTTPrequest (URL url) throws IOException{
        String jsonResponse = "";
        if(url==null)
        {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        Log.i("response ","created");
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<GlobalWarming> extractFeaturesFromJson(String jsonResponse){
        if(TextUtils.isEmpty(jsonResponse))
        {
            return null;
        }
        String url="";
        String title="";
        List<GlobalWarming> globalWarmings = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            if(results.length()>0){
                for(int i =0;i<results.length();i++) {
                    JSONObject currentNews = results.getJSONObject(i);
                    title = currentNews.getString("webTitle");
                    url=currentNews.getString("webUrl");
                    globalWarmings.add(new GlobalWarming(title,url));
                }
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }
        return globalWarmings;
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
