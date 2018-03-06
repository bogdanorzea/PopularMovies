package com.bogdanorzea.popularmovies.model.response;

import java.util.List;

import com.bogdanorzea.popularmovies.model.object.Cast;
import com.bogdanorzea.popularmovies.model.object.Crew;
import com.squareup.moshi.Json;

public class CreditsResponse {

    @Json(name = "id")
    public int id;
    @Json(name = "cast")
    public List<Cast> cast = null;
    @Json(name = "crew")
    public List<Crew> crew = null;

}