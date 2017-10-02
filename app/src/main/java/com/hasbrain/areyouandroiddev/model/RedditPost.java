package com.hasbrain.areyouandroiddev.model;

import com.hasbrain.areyouandroiddev.adapter.RedditPostAdapter;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/8/15.
 */
public class RedditPost {
    private String id;
    private String title;
    private int score;
    private int commentCount;
    private String url;
    private boolean isStickyPost;
    private String author;
    private String subreddit;
    private String domain;
    private long createdUTC;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isStickyPost() {
        return isStickyPost;
    }

    public void setIsStickyPost(boolean isStickyPost) {
        this.isStickyPost = isStickyPost;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public void setSubreddit(String subreddit) {
        this.subreddit = subreddit;
    }

    public long getCreatedUTC() {
        return createdUTC;
    }

    public void setCreatedUTC(long createdUTC) {
        this.createdUTC = createdUTC;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
