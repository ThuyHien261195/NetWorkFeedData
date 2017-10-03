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

public class DownloadTask extends AsyncTask<Void, Integer, List<RedditPost>> {

    private DownloadListener downloadListener;
    private NetworkBasedFeedDataStore networkBasedFeedDataStore;
    private List<RedditPost> redditPostList;
    private String afterId = "";

    public DownloadTask(Activity activity, String afterId) {
        this.downloadListener = (DownloadListener) activity;
        this.afterId = afterId;
    }

    @Override
    protected void onPreExecute() {
        if (downloadListener.getActiveNetwork()) {
            networkBasedFeedDataStore = new NetworkBasedFeedDataStore();
        } else {
            downloadListener.onRedditPostDownload(null, null);
            cancel(true);
        }
    }

    @Override
    protected List<RedditPost> doInBackground(Void... params) {
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
