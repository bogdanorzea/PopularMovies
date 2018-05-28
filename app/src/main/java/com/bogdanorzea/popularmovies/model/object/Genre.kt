package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(@Json(name = "id") var id: Int = 0,
                 @Json(name = "name") var name: String) : Parcelable {

    // Temporary constructor to be used from Java
    constructor(name: String) : this(0, name)

    override fun toString(): String = name

}
