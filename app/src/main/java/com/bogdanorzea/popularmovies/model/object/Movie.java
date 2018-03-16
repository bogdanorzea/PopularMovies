package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import java.util.List;

public class Movie {

    @Json(name = "adult")
    public boolean adult;
    @Json(name = "backdrop_path")
    public String backdropPath;
    @Json(name = "belongs_to_collection")
    public Object belongsToCollection;
    @Json(name = "budget")
    public int budget;
    @Json(name = "genres")
    public List<Genre> genres = null;
    @Json(name = "homepage")
    public String homepage;
    @Json(name = "id")
    public int id;
    @Json(name = "imdb_id")
    public String imdbId;
    @Json(name = "original_language")
    public String originalLanguage;
    @Json(name = "original_title")
    public String originalTitle;
    @Json(name = "overview")
    public String overview;
    @Json(name = "popularity")
    public double popularity;
    @Json(name = "poster_path")
    public String posterPath;
    @Json(name = "release_date")
    public String releaseDate;
    @Json(name = "revenue")
    public long revenue;
    @Json(name = "runtime")
    @FallbackOnNull(fallbackInt = -1)
    public int runtime;
    @Json(name = "status")
    public String status;
    @Json(name = "tagline")
    public String tagline;
    @Json(name = "title")
    public String title;
    @Json(name = "video")
    public boolean video;
    @Json(name = "vote_average")
    public double voteAverage;
    @Json(name = "vote_count")
    public int voteCount;

    public byte[] image;

    public String printGenres() {
        if (genres != null && genres.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (Genre genre : genres) {
                result.append(genre.name).append(", ");
            }

            return result.substring(0, result.length() - 2);
        } else {
            return "Unknown";
        }
    }

    public String getYear() {
        if (releaseDate != null && releaseDate.length() > 3) {
            return releaseDate.substring(0, 4);
        }

        return "";
    }
}
