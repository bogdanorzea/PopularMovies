package com.bogdanorzea.popularmovies.model.response

import com.bogdanorzea.popularmovies.model.`object`.Cast
import com.squareup.moshi.Json

data class CreditsResponse(@Json(name = "id") var id: Int = 0,
                           @Json(name = "cast") var cast: List<Cast>? = null)
