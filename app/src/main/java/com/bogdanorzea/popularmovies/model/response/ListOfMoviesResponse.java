package com.bogdanorzea.popularmovies.model.response;

import com.bogdanorzea.popularmovies.model.objects.Movie;
import com.squareup.moshi.Json;

import java.util.List;

public class ListOfMoviesResponse {

    @Json(name = "page")
    public int page;
    @Json(name = "total_results")
    public int totalResults;
    @Json(name = "total_pages")
    public int totalPages;
    @Json(name = "results")
    public List<Movie> results = null;

}
