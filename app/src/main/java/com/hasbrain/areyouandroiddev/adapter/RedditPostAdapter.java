package com.hasbrain.areyouandroiddev.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.FormatStringUtils;
import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thuyhien on 9/13/17.
 */

public class RedditPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int CONTENT_VIEW = 0;
    public static final int FOOTER_VIEW = 1;

    private List<RedditPost> redditPostList;
    private HashMap<String, String> timeTitleList;

    public RedditPostAdapter(Context context,
                             List<RedditPost> redditPostList) {
        this.redditPostList = redditPostList;
        this.timeTitleList = FormatStringUtils.createTimeTitleList(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CONTENT_VIEW:
                rowView = inflater.inflate(R.layout.item_card_view_post, parent, false);
                return new PostViewHolder(rowView, timeTitleList);
            case FOOTER_VIEW:
                rowView = inflater.inflate(R.layout.item_footer, parent, false);
                return new FooterViewHolder(rowView);
            default:
                break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case CONTENT_VIEW:
                ((PostViewHolder) holder).bindContentView(redditPostList.get(position));
                break;
            case FOOTER_VIEW:
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return redditPostList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == redditPostList.size()) {
            return FOOTER_VIEW;
        } else {
            return CONTENT_VIEW;
        }
    }
}