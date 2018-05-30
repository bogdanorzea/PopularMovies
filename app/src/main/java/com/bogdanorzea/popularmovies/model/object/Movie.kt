package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.bogdanorzea.popularmovies.utility.NetworkUtils.BACKDROP_SIZE
import com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL
import com.bogdanorzea.popularmovies.utility.NetworkUtils.POSTER_SIZE
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(@Json(name = "backdrop_path") var backdropPath: String? = null,
                 @Json(name = "budget") var budget: Int = 0,
                 @Json(name = "genres") var genres: List<Genre>? = null,
                 @Json(name = "homepage") var homepage: String? = null,
                 @Json(name = "id") var id: Int = -1,
                 @Json(name = "overview") var overview: String? = null,
                 @Json(name = "popularity") var popularity: Double = 0.0,
                 @Json(name = "poster_path") var posterPath: String?,
                 @Json(name = "release_date") var releaseDate: String? = null,
                 @Json(name = "revenue") var revenue: Long = 0L,
                 @Json(name = "runtime") var runtime: Int? = -1,
                 @Json(name = "tagline") var tagline: String? = null,
                 @Json(name = "title") var title: String?,
                 @Json(name = "vote_average") var voteAverage: Double,
                 @Json(name = "vote_count") var voteCount: Int = 0,
                 var favorite: Int = 0) : Parcelable {

    // Temporary constructor to be used from Java
    constructor() : this(null, 0, null, null, -1,
            null, 0.0, null, null, 0L, -1,
            null, null, 0.0, 0, 0)

    fun printGenres(): String = genres?.joinToString() ?: "Unknown"
    fun getYear(): String = releaseDate?.substring(0, 4) ?: ""
    fun getBackdropUrl(): String = IMAGE_BASE_URL + BACKDROP_SIZE + backdropPath
    fun getPosterUrl(): String = IMAGE_BASE_URL + POSTER_SIZE + posterPath
    fun isFavorite(): Boolean = favorite == 1

}
