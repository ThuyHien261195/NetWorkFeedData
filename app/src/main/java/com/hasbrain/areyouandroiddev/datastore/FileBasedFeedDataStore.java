package com.hasbrain.areyouandroiddev.datastore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/8/15.
 */
public class FileBasedFeedDataStore implements FeedDataStore {

    private InputStream fileInputStream;
    private Gson gson;

    public FileBasedFeedDataStore(Gson gson, InputStream jsonInputStream) {
        if (jsonInputStream == null) {
            throw new IllegalArgumentException("Json file should be provided");
        }
        this.gson = gson;
        this.fileInputStream = jsonInputStream;
    }

    @Override
    public void getPostList(String topic, String before, String after,
                            OnRedditPostsRetrievedListener onRedditPostsRetrievedListener) {
        if (onRedditPostsRetrievedListener != null) {
            Type type = new TypeToken<List<RedditPost>>() {}.getType();
            List<RedditPost> posts = gson.fromJson(new InputStreamReader(fileInputStream), type);
            onRedditPostsRetrievedListener.onRedditPostsRetrieved(posts, null);
        }
    }
}
