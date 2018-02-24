package com.bogdanorzea.popularmovies.model.objects;

import com.squareup.moshi.Json;

public class ProductionCompany {

    @Json(name = "name")
    public String name;
    @Json(name = "id")
    public int id;

}
