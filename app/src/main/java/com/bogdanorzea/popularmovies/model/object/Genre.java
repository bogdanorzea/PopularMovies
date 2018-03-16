package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

public class Genre {

    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;

}