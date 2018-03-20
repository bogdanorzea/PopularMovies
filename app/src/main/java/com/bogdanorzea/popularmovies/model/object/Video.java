package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class Video implements Parcelable {
    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
    private static final String YOUTUBE_DEFAULT_HQ_JPG_URL = "https://img.youtube.com/vi/%s/hqdefault.jpg";
    private static final String YOUTUBE = "youtube";
    @Json(name = "id")
    public String id;
    @Json(name = "key")
    public String key;
    @Json(name = "name")
    public String name;
    @Json(name = "site")
    public String site;
    @Json(name = "size")
    public int size;

    protected Video(Parcel in) {
        id = in.readString();
        key = in.readString();
        name = in.readString();
        site = in.readString();
        size = in.readInt();
    }

    public String getYoutubeThumbnailLink() {
        return String.format(YOUTUBE_DEFAULT_HQ_JPG_URL, key);
    }

    public boolean isVideoOnYoutube() {
        return site.equalsIgnoreCase(YOUTUBE);
    }

    public String getYoutubeVideoUrl() {
        return String.format("https://www.youtube.com/watch?v=%s", key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
        parcel.writeString(site);
        parcel.writeInt(size);
    }

}
