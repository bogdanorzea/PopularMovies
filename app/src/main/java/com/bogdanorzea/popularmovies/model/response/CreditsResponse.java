package com.bogdanorzea.popularmovies.model.response;

import com.bogdanorzea.popularmovies.model.object.Cast;
import com.squareup.moshi.Json;

import java.util.List;

public class CreditsResponse {

    @Json(name = "id")
    public int id;
    @Json(name = "cast")
    public List<Cast> cast = null;

}