package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

public class Cast {

    @Json(name = "cast_id")
    public int castId;
    @Json(name = "character")
    public String character;
    @Json(name = "credit_id")
    public String creditId;
    @Json(name = "gender")
    public int gender;
    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;
    @Json(name = "order")
    public int order;
    @Json(name = "profile_path")
    public Object profilePath;

}
