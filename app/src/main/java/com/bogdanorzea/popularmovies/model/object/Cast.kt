package com.bogdanorzea.popularmovies.model.`object`

import android.os.Parcelable
import com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL
import com.bogdanorzea.popularmovies.utility.NetworkUtils.PROFILE_SIZE
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Cast(@Json(name = "character") var character: String?,
                @Json(name = "gender") var gender: Int?,
                @Json(name = "id") var id: Int,
                @Json(name = "name") var name: String?,
                @Json(name = "profile_path") var profilePath: String? = null) : Parcelable {

    fun getProfileUrl() = IMAGE_BASE_URL + PROFILE_SIZE + profilePath

}
