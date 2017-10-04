package com.hasbrain.areyouandroiddev;

import com.hasbrain.areyouandroiddev.model.RedditPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by thuyhien on 10/4/17.
 */

public interface ApiEndpointInterface {
    @GET("new.json")
    Call<List<RedditPost>> getRedditPostList(@Query("limit") int limit, @Query("after") String afterId);
}
