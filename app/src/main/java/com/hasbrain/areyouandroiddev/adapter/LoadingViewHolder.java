package com.hasbrain.areyouandroiddev.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hasbrain.areyouandroiddev.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thuyhien on 10/3/17.
 */

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progress_bar_load_more)
    ProgressBar progressBarLoadMorePost;

    @BindView(R.id.text_loading)
    TextView textViewLoading;

    public LoadingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
