package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import java.util.List;

import static com.bogdanorzea.popularmovies.utility.NetworkUtils.BACKDROP_SIZE;
import static com.bogdanorzea.popularmovies.utility.NetworkUtils.IMAGE_BASE_URL;
import static com.bogdanorzea.popularmovies.utility.NetworkUtils.POSTER_SIZE;

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    @Json(name = "backdrop_path")
    public String backdropPath;
    @Json(name = "budget")
    public int budget;
    @Json(name = "genres")
    public List<Genre> genres = null;
    @Json(name = "homepage")
    public String homepage;
    @Json(name = "id")
    public int id;
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
    @Json(name = "tagline")
    public String tagline;
    @Json(name = "title")
    public String title;
    @Json(name = "vote_average")
    public double voteAverage;
    @Json(name = "vote_count")
    public int voteCount;

    public int favorite;

    public Movie() {
    }

    protected Movie(Parcel in) {
        backdropPath = in.readString();
        budget = in.readInt();
        genres = in.createTypedArrayList(Genre.CREATOR);
        homepage = in.readString();
        id = in.readInt();
        overview = in.readString();
        popularity = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
        revenue = in.readLong();
        runtime = in.readInt();
        tagline = in.readString();
        title = in.readString();
        voteAverage = in.readDouble();
        voteCount = in.readInt();
        favorite = in.readInt();
    }

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

    public String getBackdropUrl() {
        return IMAGE_BASE_URL + BACKDROP_SIZE + backdropPath;
    }

    public String getPosterUrl() {
        return IMAGE_BASE_URL + POSTER_SIZE + posterPath;
    }

    public boolean isFavorite() {
        return favorite == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(backdropPath);
        parcel.writeInt(budget);
        parcel.writeTypedList(genres);
        parcel.writeString(homepage);
        parcel.writeInt(id);
        parcel.writeString(overview);
        parcel.writeDouble(popularity);
        parcel.writeString(posterPath);
        parcel.writeString(releaseDate);
        parcel.writeLong(revenue);
        parcel.writeInt(runtime);
        parcel.writeString(tagline);
        parcel.writeString(title);
        parcel.writeDouble(voteAverage);
        parcel.writeInt(voteCount);
        parcel.writeInt(favorite);
    }

}
