package com.hasbrain.areyouandroiddev.activity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hasbrain.areyouandroiddev.DownloadTask;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.adapter.RedditPostAdapter;
import com.hasbrain.areyouandroiddev.datastore.FeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.FileBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.datastore.NetworkBasedFeedDataStore;
import com.hasbrain.areyouandroiddev.model.RedditPost;
import com.hasbrain.areyouandroiddev.model.RedditPostConverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostListActivity extends AppCompatActivity
        implements FeedDataStore.OnRedditPostsRetrievedListener {

    public static final String DATA_JSON_FILE_NAME = "data.json";
    public static final String URL_DATA_JSON = "https://www.reddit.com/r/androiddev/new.json";
    private FeedDataStore feedDataStore;

    @BindView(R.id.recycler_view_reddit_post)
    RecyclerView recyclerViewRedditPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        ButterKnife.bind(this);
        initDataByDownloading();
    }

    public void initDataByDownloading() {
        DownloadTask downloadTask = new DownloadTask(this);
        downloadTask.execute();
    }

    @Override
    public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
        if (postList != null) {
            displayPostList(postList);
        }
    }

    protected void displayPostList(List<RedditPost> postList) {
        RedditPostAdapter redditPostAdapter = new RedditPostAdapter(this, postList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewRedditPost.setLayoutManager(linearLayoutManager);
        recyclerViewRedditPost.setAdapter(redditPostAdapter);
    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

//    public void initViews() {
//        GsonBuilder gsonBuilder = new GsonBuilder();
//        gsonBuilder.registerTypeAdapter(RedditPost.class, new RedditPostConverter());
//        Gson gson = gsonBuilder.create();
//        InputStream is = null;
//        try {
//            is = getAssets().open(DATA_JSON_FILE_NAME);
//            feedDataStore = new FileBasedFeedDataStore(gson, is);
//            feedDataStore.getPostList(new FeedDataStore.OnRedditPostsRetrievedListener() {
//                @Override
//                public void onRedditPostsRetrieved(List<RedditPost> postList, Exception ex) {
//                    displayPostList(postList);
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (is != null) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
}
