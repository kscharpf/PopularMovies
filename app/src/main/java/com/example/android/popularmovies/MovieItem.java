package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MovieItem implements Parcelable {
    @Nullable
    private String mTitle;
    @Nullable
    private String mUrl;
    @Nullable
    private String mBigUrl;
    @Nullable
    private String mId;
    @Nullable
    private String mOverview;
    @Nullable
    private String mVoteAverage;
    @Nullable
    private String mReleaseDate;

    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        public MovieItem createFromParcel(@NonNull Parcel in) {
            return new MovieItem(in);
        }

        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public MovieItem() {

    }
    private MovieItem(Parcel in) {
        mTitle = in.readString();
        mUrl = in.readString();
        mBigUrl = in.readString();
        mId = in.readString();
        mOverview = in.readString();
        mVoteAverage = in.readString();
        mReleaseDate = in.readString();
    }

    @Nullable
    public String getTitle() {
        return mTitle;
    }

    public void setTitle(@Nullable String title) {
        mTitle = title;
    }

    @Nullable
    public String getUrl() {
        return mUrl;
    }

    public void setUrl(@Nullable String url) {
        mUrl = url;
    }

    @Nullable
    public String getBigUrl() {
        return mBigUrl;
    }

    public void setBigUrl(@Nullable String bigUrl) {
        mBigUrl = bigUrl;
    }

    public void setId(@Nullable String id) {
        mId = id;
    }

    @Nullable
    public String getOverview() {
        return mOverview;
    }

    public void setOverview(@Nullable String overview) {
        mOverview = overview;
    }

    @Nullable
    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(@Nullable String voteAverage) {
        mVoteAverage = voteAverage;
    }

    @Nullable
    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(@Nullable String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public String toString() {
        return mTitle;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mUrl);
        dest.writeString(mBigUrl);
        dest.writeString(mId);
        dest.writeString(mOverview);
        dest.writeString(mVoteAverage);
        dest.writeString(mReleaseDate);
    }
}
