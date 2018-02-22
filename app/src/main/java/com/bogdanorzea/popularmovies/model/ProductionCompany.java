package com.bogdanorzea.popularmovies.model;

import com.squareup.moshi.Json;

public class ProductionCompany {

    @Json(name = "name")
    public String name;
    @Json(name = "id")
    public int id;

    public ProductionCompany withName(String name) {
        this.name = name;
        return this;
    }

    public ProductionCompany withId(int id) {
        this.id = id;
        return this;
    }

}
