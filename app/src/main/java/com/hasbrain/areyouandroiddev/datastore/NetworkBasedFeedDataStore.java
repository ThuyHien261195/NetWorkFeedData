package com.hasbrain.areyouandroiddev.datastore;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hasbrain.areyouandroiddev.ApiEndpointInterface;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;
import com.hasbrain.areyouandroiddev.model.RedditPostListConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by thuyhien on 10/2/17.
 */

public class NetworkBasedFeedDataStore implements FeedDataStore {

    private static final int READ_TIME_OUT = 3000;
    private static final int CONNECT_TIME_OUT = 3000;
    public static final String BASE_URL = "https://www.reddit.com/r/androiddev/";
    private static final int LOADING_ITEM_LIMIT = 8;

    @Override
    public void getPostList(String topic, String before, String after,
                            OnRedditPostsRetrievedListener onRedditPostsRetrievedListener) {
        try {
            List<RedditPost> redditPostList = downloadFromUrlByRetrofit(after);
            onRedditPostsRetrievedListener.onRedditPostsRetrieved(redditPostList, null);
        } catch (IOException e) {
            onRedditPostsRetrievedListener.onRedditPostsRetrieved(null, e);
        }
    }

    private List<RedditPost> downloadFromUrlByRetrofit(String afterId) throws IOException {
        List<RedditPost> redditPostList = null;
        Type type = new TypeToken<List<RedditPost>>() {
        }.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, new RedditPostListConverter());
        Gson gson = gsonBuilder.create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiEndpointInterface apiService = retrofit.create(ApiEndpointInterface.class);

        Call<List<RedditPost>> call = apiService.getRedditPostList(LOADING_ITEM_LIMIT, afterId);

        Response<List<RedditPost>> redditPostListResponse = call.execute();
        if (redditPostListResponse.isSuccessful()) {
            redditPostList = redditPostListResponse.body();
        }
        return redditPostList;
    }

    private String setBaseUrl(String afterId) {
        String downloadUrl = BASE_URL + "new.json" + "?limit=" + LOADING_ITEM_LIMIT;
        if (!afterId.equals("")) {
            downloadUrl += "&after=" + afterId;
        }
        return downloadUrl;
    }

    private List<RedditPost> downloadFromUrl(String downloadUrl) throws IOException {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        List<RedditPost> redditPostList = null;

        try {
            URL url = new URL(downloadUrl);
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
