package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.bogdanorzea.popularmovies.utility.NetworkUtils.BACKDROP_SIZE
import com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL
import com.bogdanorzea.popularmovies.utility.NetworkUtils.POSTER_SIZE
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movie(@Json(name = "backdrop_path") var backdropPath: String?,
                 @Json(name = "budget") var budget: Int = 0,
                 @Json(name = "genres") var genres: List<Genre>?,
                 @Json(name = "homepage") var homepage: String?,
                 @Json(name = "id") var id: Int,
                 @Json(name = "overview") var overview: String?,
                 @Json(name = "popularity") var popularity: Double,
                 @Json(name = "poster_path") var posterPath: String?,
                 @Json(name = "release_date") var releaseDate: String,
                 @Json(name = "revenue") var revenue: Long = 0L,
                 @Json(name = "runtime") var runtime: Int?,
                 @Json(name = "tagline") var tagline: String?,
                 @Json(name = "title") var title: String,
                 @Json(name = "vote_average") var voteAverage: Double,
                 @Json(name = "vote_count") var voteCount: Int,
                 var favorite: Int = 0) : Parcelable {

    fun printGenres(): String = genres?.joinToString() ?: "Unknown"
    fun getYear(): String = if (releaseDate.length >= 4) releaseDate.substring(0, 4) else ""
    fun getBackdropUrl(): String = IMAGE_BASE_URL + BACKDROP_SIZE + backdropPath
    fun getPosterUrl(): String = IMAGE_BASE_URL + POSTER_SIZE + posterPath
    fun isFavorite(): Boolean = favorite == 1

}
