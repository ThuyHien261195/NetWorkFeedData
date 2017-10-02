package com.hasbrain.areyouandroiddev;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.NetworkBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by thuyhien on 10/2/17.
 */

public class DownloadTask extends AsyncTask<Void, Integer, List<RedditPost>> {

    public static final String URL_DATA_JSON = "https://www.reddit.com/r/androiddev/new.json";

    private FeedDataStore.OnRedditPostsRetrievedListener retrievedListener;
    private NetworkBasedFeedDataStore networkBasedFeedDataStore;
    private List<RedditPost> redditPostList;

    public DownloadTask(FeedDataStore.OnRedditPostsRetrievedListener retrievedListener) {
        this.retrievedListener = retrievedListener;
    }

    @Override
    protected void onPreExecute() {
        networkBasedFeedDataStore = new NetworkBasedFeedDataStore();
        networkBasedFeedDataStore.setBaseUrl(URL_DATA_JSON);
    }

    @Override
    protected List<RedditPost> doInBackground(Void... params) {
        networkBasedFeedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
            @Override
            public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                getRedditPostListFromData(postList);
            }
        });
        return redditPostList;
    }

    @Override
    protected void onPostExecute(List<RedditPost> result) {
        retrievedListener.onRedditPostsRetrieved(result, null);
    }

    private void getRedditPostListFromData(List<RedditPost> redditPostList) {
        this.redditPostList = redditPostList;
    }
}
