package com.bogdanorzea.popularmovies.model;

import com.squareup.moshi.Json;

public class ProductionCountry {

    @Json(name = "iso_3166_1")
    public String iso31661;
    @Json(name = "name")
    public String name;

    public ProductionCountry withIso31661(String iso31661) {
        this.iso31661 = iso31661;
        return this;
    }

    public ProductionCountry withName(String name) {
        this.name = name;
        return this;
    }

}
