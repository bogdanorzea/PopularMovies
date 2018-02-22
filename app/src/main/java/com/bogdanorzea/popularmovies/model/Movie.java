package com.bogdanorzea.popularmovies.model;

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
    public int revenue;
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
    public double voteAverage;
    @Json(name = "vote_count")
    public int voteCount;

    public Movie withAdult(boolean adult) {
        this.adult = adult;
        return this;
    }

    public Movie withBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public Movie withBelongsToCollection(Object belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
        return this;
    }

    public Movie withBudget(int budget) {
        this.budget = budget;
        return this;
    }

    public Movie withGenres(List<Genre> genres) {
        this.genres = genres;
        return this;
    }

    public Movie withHomepage(String homepage) {
        this.homepage = homepage;
        return this;
    }

    public Movie withId(int id) {
        this.id = id;
        return this;
    }

    public Movie withImdbId(String imdbId) {
        this.imdbId = imdbId;
        return this;
    }

    public Movie withOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
        return this;
    }

    public Movie withOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public Movie withOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Movie withPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    public Movie withPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public Movie withProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
        return this;
    }

    public Movie withProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
        return this;
    }

    public Movie withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public Movie withRevenue(int revenue) {
        this.revenue = revenue;
        return this;
    }

    public Movie withRuntime(int runtime) {
        this.runtime = runtime;
        return this;
    }

    public Movie withSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
        return this;
    }

    public Movie withStatus(String status) {
        this.status = status;
        return this;
    }

    public Movie withTagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public Movie withTitle(String title) {
        this.title = title;
        return this;
    }

    public Movie withVideo(boolean video) {
        this.video = video;
        return this;
    }

    public Movie withVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public Movie withVoteCount(int voteCount) {
        this.voteCount = voteCount;
        return this;
    }

}
