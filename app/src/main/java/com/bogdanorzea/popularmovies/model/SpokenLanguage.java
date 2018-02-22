package com.bogdanorzea.popularmovies.model;

import com.squareup.moshi.Json;

public class SpokenLanguage {

    @Json(name = "iso_639_1")
    public String iso6391;
    @Json(name = "name")
    public String name;

    public SpokenLanguage withIso6391(String iso6391) {
        this.iso6391 = iso6391;
        return this;
    }

    public SpokenLanguage withName(String name) {
        this.name = name;
        return this;
    }

}
