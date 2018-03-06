package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

import java.util.List;

public class TruncatedMovie {

    @Json(name = "poster_path")
    public String posterPath;
    @Json(name = "adult")
    public boolean adult;
    @Json(name = "overview")
    public String overview;
    @Json(name = "release_date")
    public String releaseDate;
    @Json(name = "genre_ids")
    public List<Integer> genreIds = null;
    @Json(name = "id")
    public int id;
    @Json(name = "original_title")
    public String originalTitle;
    @Json(name = "original_language")
    public String originalLanguage;
    @Json(name = "title")
    public String title;
    @Json(name = "backdrop_path")
    public String backdropPath;
    @Json(name = "popularity")
    public double popularity;
    @Json(name = "vote_count")
    public int voteCount;
    @Json(name = "video")
    public boolean video;
    @Json(name = "vote_average")
    public double voteAverage;

}
