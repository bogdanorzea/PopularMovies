package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Review(@Json(name = "id") var id: String,
                  @Json(name = "author") var author: String,
                  @Json(name = "content") var content: String,
                  @Json(name = "url") var url: String) : Parcelable
