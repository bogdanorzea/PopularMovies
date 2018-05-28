package com.bogdanorzea.popularmovies.model.response

import com.bogdanorzea.popularmovies.model.`object`.Movie
import com.squareup.moshi.Json

data class MoviesResponse(@Json(name = "page") var page: Int = 0,
                          @Json(name = "total_results") var totalResults: Int = 0,
                          @Json(name = "total_pages") var totalPages: Int = 0,
                          @Json(name = "results") var results: List<Movie>? = null)
