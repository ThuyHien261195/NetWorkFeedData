package com.hasbrain.areyouandroiddev;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.NetworkBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by thuyhien on 10/2/17.
 */

public class DownloadTask extends AsyncTask<String, Integer, List<RedditPost>> {

    private DownloadListener downloadListener;
    private FeedDataStore networkBasedFeedDataStore;
    private List<RedditPost> redditPostList;

    public DownloadTask(DownloadListener downloadListener, FeedDataStore networkBasedFeedDataStore) {
        this.downloadListener = downloadListener;
        this.networkBasedFeedDataStore = networkBasedFeedDataStore;
    }

    @Override
    protected List<RedditPost> doInBackground(String... params) {
        String afterId = params[0];
        networkBasedFeedDataStore.getPostList(null, null, afterId, new FeedDataStore.OnRedditPostsRetrievedListener() {
            @Override
            public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
                getRedditPostListFromData(postList);
            }
        });
        return redditPostList;
    }

    @Override
    protected void onPostExecute(List<RedditPost> result) {
        downloadListener.onRedditPostDownload(result, null);
    }

    private void getRedditPostListFromData(List<RedditPost> redditPostList) {
        this.redditPostList = redditPostList;
    }
}
