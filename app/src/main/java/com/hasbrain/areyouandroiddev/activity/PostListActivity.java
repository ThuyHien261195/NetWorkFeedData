package com.hasbrain.areyouandroiddev.activity;

import com.hasbrain.areyouandroiddev.DownloadListener;
import com.hasbrain.areyouandroiddev.DownloadTask;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.adapter.RedditPostAdapter;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PostListActivity extends AppCompatActivity
        implements DownloadListener {
    public static final int FIRST_LOAD = 0;
    public static final int REFRESH = 1;
    public static final int LOAD_MORE = 2;
    public static final int LOAD_MORE_DONE = 3;

    @BindView(R.id.swipe_post_container)
    SwipeRefreshLayout swipeLayoutRedditPost;

    @BindView(R.id.recycler_view_reddit_post)
    RecyclerView recyclerViewRedditPost;

    @BindView(R.id.layout_error_no_posts)
    LinearLayout linearLayoutErrorNoPosts;

    private int loadingState;
    private RedditPostAdapter redditPostAdapter;
    private String afterId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());

        ButterKnife.bind(this);

        initDataByDownloading();
        initView();
    }

    @Override
    public void onRedditPostDownload(List<RedditPost> postList, Exception ex) {
        displayPostList(postList);
    }

    @Override
    public boolean getActiveNetwork() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()
                && (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE
                || networkInfo.getType() == ConnectivityManager.TYPE_WIFI)) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.button_try)
    public void onClickTryLoading() {
        setLoadingState(FIRST_LOAD);
    }

    protected void displayPostList(List<RedditPost> postList) {
        switch (loadingState) {
            case FIRST_LOAD:
                if (postList != null) {
                    setStateForUI(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
                    redditPostAdapter = new RedditPostAdapter(this, postList);
                    recyclerViewRedditPost.setLayoutManager(linearLayoutManager);
                    recyclerViewRedditPost.setAdapter(redditPostAdapter);
                } else {
                    setStateForUI(false);
                }
                break;
            case REFRESH:
                swipeLayoutRedditPost.setRefreshing(false);
                if (postList != null) {
                    redditPostAdapter.updatePostListData(loadingState, postList);
                    recyclerViewRedditPost.scrollToPosition(0);
                }
                break;
            case LOAD_MORE:
                loadingState = LOAD_MORE_DONE;
                redditPostAdapter.removeLoadingItem();
                if (postList != null) {
                    redditPostAdapter.updatePostListData(loadingState, postList);
                }
                break;
        }
        if (postList != null) {
            afterId = "t3_" + postList.get(postList.size() - 1).getId();
        }

        initViewAfterDownloading();
    }

    protected int getLayoutResource() {
        return R.layout.activity_post_list;
    }

    private void initView() {
        swipeLayoutRedditPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setLoadingState(REFRESH);
            }
        });
        swipeLayoutRedditPost.setColorSchemeColors(ContextCompat.getColor(this, android.R.color.holo_blue_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_red_light));
    }

    private void initDataByDownloading() {
        DownloadTask downloadTask = new DownloadTask(this, afterId);
        downloadTask.execute();
    }

    private void setStateForUI(boolean visible) {
        swipeLayoutRedditPost.setEnabled(visible);
        if (visible) {
            recyclerViewRedditPost.setVisibility(View.VISIBLE);
            linearLayoutErrorNoPosts.setVisibility(View.GONE);
        } else {
            recyclerViewRedditPost.setVisibility(View.GONE);
            linearLayoutErrorNoPosts.setVisibility(View.VISIBLE);
        }
    }

    private void setLoadingState(int state) {
        loadingState = state;
        initDataByDownloading();
    }

    private void initViewAfterDownloading() {
        final LinearLayoutManager linearLayoutManager =
                (LinearLayoutManager) recyclerViewRedditPost.getLayoutManager();
        recyclerViewRedditPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int visibleItems, firstVisibleItemPos, totalItems;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && linearLayoutManager != null) {
                    visibleItems = linearLayoutManager.getChildCount();
                    firstVisibleItemPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    totalItems = linearLayoutManager.getItemCount();
                    if (loadingState != LOAD_MORE
                            && getActiveNetwork()
                            && (visibleItems + firstVisibleItemPos >= totalItems)) {
                        redditPostAdapter.addLoadingItem();
                        setLoadingState(LOAD_MORE);
                    }
                }
            }
        });
    }
}
