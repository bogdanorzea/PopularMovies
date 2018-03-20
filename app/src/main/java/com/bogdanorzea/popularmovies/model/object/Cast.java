package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import static com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL;
import static com.bogdanorzea.popularmovies.utility.NetworkUtils.PROFILE_SIZE;

public class Cast implements Parcelable {

    @Json(name = "character")
    public String character;
    @Json(name = "gender")
    @FallbackOnNull(fallbackInt = -1)
    public int gender;
    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;
    @Json(name = "profile_path")
    public String profilePath;


    protected Cast(Parcel in) {
        character = in.readString();
        gender = in.readInt();
        id = in.readInt();
        name = in.readString();
        profilePath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(character);
        dest.writeInt(gender);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(profilePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Cast> CREATOR = new Creator<Cast>() {
        @Override
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        @Override
        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public String getProfileUrl() {
        return IMAGE_BASE_URL + PROFILE_SIZE + profilePath;
    }

}
