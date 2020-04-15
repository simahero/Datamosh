package com.simahero.datamosh.Fragments.CREATE;

import android.graphics.Bitmap;

public class PFrame {

    String name;
    String extension;
    int count;
    int timestamp;
    Bitmap thumbnail;

    public PFrame(int count, int timestamp, Bitmap thumbnail) {
        this.count = count;
        this.timestamp = timestamp;
        this.thumbnail = thumbnail;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
