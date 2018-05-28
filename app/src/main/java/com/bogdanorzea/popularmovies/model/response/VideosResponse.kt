package com.bogdanorzea.popularmovies.model.response

import com.bogdanorzea.popularmovies.model.`object`.Video
import com.squareup.moshi.Json

class VideosResponse(@Json(name = "id") var id: Int = 0,
                     @Json(name = "results") var results: List<Video>? = null)
