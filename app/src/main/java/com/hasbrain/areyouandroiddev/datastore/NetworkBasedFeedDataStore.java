package com.hasbrain.areyouandroiddev.datastore;

import android.content.res.TypedArray;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by thuyhien on 10/2/17.
 */

public class NetworkBasedFeedDataStore implements FeedDataStore {

    private static final int READ_TIME_OUT = 3000;
    private static final int CONNECT_TIME_OUT = 3000;
    private String baseUrl;

    @Override
    public void getPostList(OnRedditPostsRetrievedListener onRedditPostsRetrievedListener) {
        List<RedditPost> redditPostList = null;
        try {
            redditPostList = downloadFromUrl(baseUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onRedditPostsRetrievedListener.onRedditPostsRetrieved(redditPostList, null);
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<RedditPost> downloadFromUrl(String stringUrl) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        List<RedditPost> redditPostList = null;

        try {
            URL url = new URL(stringUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(READ_TIME_OUT);
            connection.setConnectTimeout(CONNECT_TIME_OUT);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int requestCode = connection.getResponseCode();
            if (requestCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + requestCode);
            }
            inputStream = connection.getInputStream();
            if (inputStream != null) {
                redditPostList = convertDataGsonToModel(inputStream);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return redditPostList;
    }

    private List<RedditPost> convertDataGsonToModel(InputStream inputStream) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
        Gson gson = gsonBuilder.create();
        try {
            String dataJson = getDataJson(inputStream);
            try {
                JSONObject json = new JSONObject(dataJson);
                JSONArray redditPostsJson = json.getJSONObject("data").getJSONArray("children");
                if (redditPostsJson != null) {
                    Type type = new TypeToken<List<RedditPost>>() {
                    }.getType();
                    return gson.fromJson(redditPostsJson.toString(), type);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getDataJson(InputStream inputStream) throws IOException {
        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }
}
