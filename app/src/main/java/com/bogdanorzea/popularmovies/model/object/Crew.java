package com.bogdanorzea.popularmovies.model.object;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

public class Crew {

    @Json(name = "credit_id")
    public String creditId;
    @Json(name = "department")
    public String department;
    @Json(name = "gender")
    @FallbackOnNull(fallbackInt = -1)
    public int gender;
    @Json(name = "id")
    public int id;
    @Json(name = "job")
    public String job;
    @Json(name = "name")
    public String name;
    @Json(name = "profile_path")
    public String profilePath;

}