package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(@Json(name = "id") var id: Int,
                 @Json(name = "name") var name: String) : Parcelable {

    override fun toString(): String = name

}
