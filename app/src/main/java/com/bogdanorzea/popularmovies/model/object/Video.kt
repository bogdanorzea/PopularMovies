package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(@Json(name = "id") var id: String?,
                 @Json(name = "key") var key: String?,
                 @Json(name = "name") var name: String?,
                 @Json(name = "site") var site: String?,
                 @Json(name = "size") var size: Int = 0) : Parcelable {

    fun getYoutubeThumbnailLink() = "https://img.youtube.com/vi/$key/hqdefault.jpg"
    fun isVideoOnYoutube() = site.equals("youtube", ignoreCase = true)
    fun getYoutubeVideoUrl() = "https://www.youtube.com/watch?v=$key"

}
