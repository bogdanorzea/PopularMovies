package com.bogdanorzea.popularmovies.model.object;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import static com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL;
import static com.bogdanorzea.popularmovies.utility.NetworkUtils.PROFILE_SIZE;

public class Cast {

    @Json(name = "cast_id")
    public int castId;
    @Json(name = "character")
    public String character;
    @Json(name = "credit_id")
    public String creditId;
    @Json(name = "gender")
    @FallbackOnNull(fallbackInt = -1)
    public int gender;
    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;
    @Json(name = "order")
    public int order;
    @Json(name = "profile_path")
    public String profilePath;

    public String getProfileUrl() {
        return IMAGE_BASE_URL + PROFILE_SIZE + profilePath;
    }
}
