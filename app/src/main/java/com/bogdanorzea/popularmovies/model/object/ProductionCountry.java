package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

public class ProductionCountry {

    @Json(name = "iso_3166_1")
    public String iso31661;
    @Json(name = "name")
    public String name;

}
