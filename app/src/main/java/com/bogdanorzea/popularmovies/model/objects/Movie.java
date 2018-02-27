package com.bogdanorzea.popularmovies.model.objects;

import com.squareup.moshi.Json;

import java.util.List;

public class Movie {

    @Json(name = "adult")
    public boolean adult;
    @Json(name = "backdrop_path")
    public String backdropPath;
    @Json(name = "belongs_to_collection")
    public Object belongsToCollection;
    @Json(name = "budget")
    public int budget;
    @Json(name = "genres")
    public List<Genre> genres = null;
    @Json(name = "homepage")
    public String homepage;
    @Json(name = "id")
    public int id;
    @Json(name = "imdb_id")
    public String imdbId;
    @Json(name = "original_language")
    public String originalLanguage;
    @Json(name = "original_title")
    public String originalTitle;
    @Json(name = "overview")
    public String overview;
    @Json(name = "popularity")
    public double popularity;
    @Json(name = "poster_path")
    public String posterPath;
    @Json(name = "production_companies")
    public List<ProductionCompany> productionCompanies = null;
    @Json(name = "production_countries")
    public List<ProductionCountry> productionCountries = null;
    @Json(name = "release_date")
    public String releaseDate;
    @Json(name = "revenue")
    public long revenue;
    @Json(name = "runtime")
    public int runtime;
    @Json(name = "spoken_languages")
    public List<SpokenLanguage> spokenLanguages = null;
    @Json(name = "status")
    public String status;
    @Json(name = "tagline")
    public String tagline;
    @Json(name = "title")
    public String title;
    @Json(name = "video")
    public boolean video;
    @Json(name = "vote_average")
    public float voteAverage;
    @Json(name = "vote_count")
    public int voteCount;

}
