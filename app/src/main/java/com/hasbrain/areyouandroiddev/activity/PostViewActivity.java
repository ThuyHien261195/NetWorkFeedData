package com.hasbrain.areyouandroiddev;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostViewActivity extends AppCompatActivity {
    @BindView(R.id.web_view_reddit_post)
    WebView webViewRedditPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        String url = getIntent().getStringExtra(ConstantCollection.BUNDLE_URL);
        webViewRedditPost.loadUrl(url);
    }
}
