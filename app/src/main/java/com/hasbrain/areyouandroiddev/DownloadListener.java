package com.hasbrain.areyouandroiddev;

import android.net.NetworkInfo;

import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

/**
 * Created by thuyhien on 10/3/17.
 */

public interface DownloadListener {
    void onRedditPostDownload(List<RedditPost> redditPostList, Exception ex);

    boolean getActiveNetwork();
}
