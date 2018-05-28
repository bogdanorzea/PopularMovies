package com.bogdanorzea.popularmovies.model.response

import com.bogdanorzea.popularmovies.model.`object`.Review
import com.squareup.moshi.Json

class ReviewsResponse(@Json(name = "id") var id: Int = 0,
                      @Json(name = "page") var page: Int = 0,
                      @Json(name = "results") var results: List<Review>? = null,
                      @Json(name = "total_pages") var totalPages: Int = 0,
                      @Json(name = "total_results") var totalResults: Int = 0)
