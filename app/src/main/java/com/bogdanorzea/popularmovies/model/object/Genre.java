package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class Genre implements Parcelable {
    public Genre(String name) {
        this.name = name;
    }

    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;

    protected Genre(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
    }
}