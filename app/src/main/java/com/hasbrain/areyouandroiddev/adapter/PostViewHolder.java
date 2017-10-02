package com.hasbrain.areyouandroiddev.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.FormatStringUtils;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.activity.PostViewActivity;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.net.URL;
import java.util.HashMap;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thuyhien on 10/2/17.
 */

public class PostViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text_score)
    TextView textViewScore;

    @BindView(R.id.text_author)
    TextView textViewAuthor;

    @BindView(R.id.text_post_title)
    TextView textViewPostTitle;

    @BindView(R.id.text_comment)
    TextView textViewComment;

    @BindString(R.string.title_comment)
    String titleComment;

    @BindString(R.string.title_author)
    String titleAuthor;

    @BindColor(R.color.color_author_title)
    int colorAuthorPost;

    @BindColor(R.color.color_sticky_post)
    int colorStickyPost;

    @BindColor(R.color.color_normal_post)
    int colorNormalPost;

    private RedditPost redditPost;
    private HashMap<String, String> timeTitleList;

    public PostViewHolder(final View itemView, HashMap<String, String> timeTitleList) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.timeTitleList = timeTitleList;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostViewActivity.class);
                intent.putExtra(FooterViewHolder.EXTRA_NAME_URL, redditPost.getUrl());
                v.getContext().startActivity(intent);
            }
        });
    }

    public void bindContentView(RedditPost redditPost) {
        this.redditPost = redditPost;
        String postTime = FormatStringUtils.getPostTime(redditPost.getCreatedUTC(), timeTitleList);
        textViewScore.setText(String.valueOf(redditPost.getScore()));
        textViewPostTitle.setText(redditPost.getTitle());
        if (redditPost.isStickyPost()) {
            textViewPostTitle.setTextColor(colorStickyPost);
        } else {
            textViewPostTitle.setTextColor(colorNormalPost);
        }
        textViewComment.setText(String.format(titleComment,
                redditPost.getCommentCount(),
                redditPost.getDomain(),
                postTime));
        bindAuthorText();
    }

    protected void bindAuthorText() {
        String authorTitle = FormatStringUtils.formatAuthorTitle(
                titleAuthor,
                redditPost.getAuthor(),
                redditPost.getSubreddit(),
                colorAuthorPost);
        textViewAuthor.setText(Html.fromHtml(authorTitle));
    }
}
