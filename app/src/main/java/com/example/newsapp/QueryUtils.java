package com.example.newsapp;

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
import java.util.ArrayList;

public final class QueryUtils {

    private static final String LOGTAG = QueryUtils.class.getName();

    public QueryUtils(){}

    public static ArrayList<News> fetchNewsData(String requestURL){

        ArrayList<News> news = new ArrayList<>();
        String jsonResponse;

        URL url = createURL(requestURL);
        try {
            jsonResponse = makeHttpRequest(url);
            news = extractFromJSON(jsonResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return news;
    }

    private static URL createURL(String stringUrl){
        if (stringUrl == null){
            Log.e(LOGTAG, "String received at createURL() method is null");
            return null;
        }

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            Log.e(LOGTAG, "NULL URL passed in the makeHttpRequest() method");
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOGTAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOGTAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        InputStreamReader streamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(streamReader);
        String line = reader.readLine();

        while (line != null){
            output.append(line);
            line = reader.readLine();
        }

        return output.toString();
    }

    public static ArrayList<News> extractFromJSON(String jsonString){

        if (jsonString == null){
            return null;
        }

        ArrayList<News> news = null;

        try {
            news = new ArrayList<>();

            JSONObject rootJsonObject = new JSONObject(jsonString);
            JSONObject responseJsonObject = rootJsonObject.optJSONObject("response");
            JSONArray resultsJsonArray = responseJsonObject.optJSONArray("results");

            if (resultsJsonArray != null){
                for (int i = 0; i < resultsJsonArray.length(); i++){
                    JSONObject newsJsonObject = resultsJsonArray.optJSONObject(i);
                    String title = newsJsonObject.optString("webTitle");
                    String section = newsJsonObject.optString("sectionName");
                    String date = newsJsonObject.optString("webPublicationDate");
                    String webUrl = newsJsonObject.optString("webUrl");

                    JSONArray authorJsonArray = newsJsonObject.optJSONArray("references");
                    JSONObject authorJsonObject = null;
                    String author = null;
                    if (authorJsonArray != null) {
                        authorJsonObject = authorJsonArray.optJSONObject(0);
                        if (authorJsonObject != null){
                            author = authorJsonObject.optString("id");
                        }
                    }

                    news.add(new News(title,section,date,author,webUrl));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }
}
