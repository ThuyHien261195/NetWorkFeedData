package com.hasbrain.areyouandroiddev.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.hasbrain.areyouandroiddev.R;
import com.hasbrain.areyouandroiddev.activity.PostViewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thuyhien on 10/2/17.
 */

public class FooterViewHolder extends RecyclerView.ViewHolder{
    public static final String EXTRA_NAME_URL = "url";
    public static final String EXTRA_VALUE_MORE_INFO_URL = "https://www.reddit.com/r/androiddev/";

    @BindView(R.id.text_footer)
    TextView textViewFooter;

    public FooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this. itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostViewActivity.class);
                intent.putExtra(EXTRA_NAME_URL, EXTRA_VALUE_MORE_INFO_URL);
                v.getContext().startActivity(intent);
            }
        });
    }
}
