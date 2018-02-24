package com.bogdanorzea.popularmovies.model.objects;

import com.squareup.moshi.Json;

public class SpokenLanguage {

    @Json(name = "iso_639_1")
    public String iso6391;
    @Json(name = "name")
    public String name;

}
