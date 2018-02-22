package com.bogdanorzea.popularmovies.model;

import com.squareup.moshi.Json;

public class Genre {

    @Json(name = "id")
    public int id;
    @Json(name = "name")
    public String name;

    public Genre withId(int id) {
        this.id = id;
        return this;
    }

    public Genre withName(String name) {
        this.name = name;
        return this;
    }
}
