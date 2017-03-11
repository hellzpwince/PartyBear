package com.discoverfriend.partybear;

/**
 * Created by mukesh on 20/02/17.
 */

public class RecentModel {
    String imageurl;
    long timestamp;

    public RecentModel(String imageurl, long timestamp) {
        this.imageurl = imageurl;
        this.timestamp = timestamp;
    }

    public RecentModel() {
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
