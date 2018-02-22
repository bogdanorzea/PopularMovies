package com.bogdanorzea.popularmovies.model;

import com.squareup.moshi.Json;

import java.util.List;

public class Popular {

    @Json(name = "page")
    public int page;
    @Json(name = "total_results")
    public int totalResults;
    @Json(name = "total_pages")
    public int totalPages;
    @Json(name = "results")
    public List<Movie> results = null;

    public Popular withPage(int page) {
        this.page = page;
        return this;
    }

    public Popular withTotalResults(int totalResults) {
        this.totalResults = totalResults;
        return this;
    }

    public Popular withTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public Popular withMovies(List<Movie> movies) {
        this.results = movies;
        return this;
    }

}
