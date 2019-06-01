package com.example.baking_app_project.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Step implements Parcelable {
    private final String shortDescription;
    private final String description;
    private final String videoUrl;
    private final String thumbnailUrl;

    public Step(String shortDescription, String description, String videoUrl, String thumbnailUrl){
        this.shortDescription = shortDescription;
        this.description = description;
        this.videoUrl = videoUrl;
        this.thumbnailUrl = thumbnailUrl;
    }

    private Step(Parcel in) {
        shortDescription = in.readString();
        description = in.readString();
        videoUrl = in.readString();
        thumbnailUrl = in.readString();
    }

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public String getShortDescription(){
        return this.shortDescription;
    }

    public String getDescription(){
        return this.description;
    }

    public String getVideoUrl(){
        return this.videoUrl;
    }

    public String getThumbnailUrl(){
        return this.thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoUrl);
        dest.writeString(thumbnailUrl);
    }
}
