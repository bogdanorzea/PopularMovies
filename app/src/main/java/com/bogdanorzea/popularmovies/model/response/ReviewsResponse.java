package com.bogdanorzea.popularmovies.model.response;

import java.util.List;

import com.bogdanorzea.popularmovies.model.object.Review;
import com.squareup.moshi.Json;

public class ReviewsResponse {

    @Json(name = "id")
    public int id;
    @Json(name = "page")
    public int page;
    @Json(name = "results")
    public List<Review> results = null;
    @Json(name = "total_pages")
    public int totalPages;
    @Json(name = "total_results")
    public int totalResults;

}