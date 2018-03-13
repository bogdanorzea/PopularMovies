package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.serjltt.moshi.adapters.FallbackOnNull;
import com.squareup.moshi.Json;

import java.util.List;

public class Movie implements Parcelable {

    public final static Parcelable.Creator<Movie> CREATOR = new Creator<Movie>() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return (new Movie[size]);
        }

    };
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
    @Json(name = "production_companies")
    public List<ProductionCompany> productionCompanies = null;
    @Json(name = "production_countries")
    public List<ProductionCountry> productionCountries = null;
    @Json(name = "release_date")
    public String releaseDate;
    @Json(name = "revenue")
    public long revenue;
    @Json(name = "runtime")
    @FallbackOnNull(fallbackInt = -1)
    public int runtime;
    @Json(name = "spoken_languages")
    public List<SpokenLanguage> spokenLanguages = null;
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

    protected Movie(Parcel in) {
        this.adult = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.backdropPath = ((String) in.readValue((String.class.getClassLoader())));
        this.belongsToCollection = ((Object) in.readValue((Object.class.getClassLoader())));
        this.budget = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.genres, (com.bogdanorzea.popularmovies.model.object.Genre.class.getClassLoader()));
        this.homepage = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
        this.imdbId = ((String) in.readValue((String.class.getClassLoader())));
        this.originalLanguage = ((String) in.readValue((String.class.getClassLoader())));
        this.originalTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.overview = ((String) in.readValue((String.class.getClassLoader())));
        this.popularity = ((double) in.readValue((double.class.getClassLoader())));
        this.posterPath = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.productionCompanies, (com.bogdanorzea.popularmovies.model.object.ProductionCompany.class.getClassLoader()));
        in.readList(this.productionCountries, (com.bogdanorzea.popularmovies.model.object.ProductionCountry.class.getClassLoader()));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.revenue = ((long) in.readValue((long.class.getClassLoader())));
        this.runtime = ((int) in.readValue((int.class.getClassLoader())));
        in.readList(this.spokenLanguages, (com.bogdanorzea.popularmovies.model.object.SpokenLanguage.class.getClassLoader()));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.tagline = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        this.video = ((boolean) in.readValue((boolean.class.getClassLoader())));
        this.voteAverage = ((double) in.readValue((double.class.getClassLoader())));
        this.voteCount = ((int) in.readValue((int.class.getClassLoader())));
    }

    public Movie() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(adult);
        dest.writeValue(backdropPath);
        dest.writeValue(belongsToCollection);
        dest.writeValue(budget);
        dest.writeList(genres);
        dest.writeValue(homepage);
        dest.writeValue(id);
        dest.writeValue(imdbId);
        dest.writeValue(originalLanguage);
        dest.writeValue(originalTitle);
        dest.writeValue(overview);
        dest.writeValue(popularity);
        dest.writeValue(posterPath);
        dest.writeList(productionCompanies);
        dest.writeList(productionCountries);
        dest.writeValue(releaseDate);
        dest.writeValue(revenue);
        dest.writeValue(runtime);
        dest.writeList(spokenLanguages);
        dest.writeValue(status);
        dest.writeValue(tagline);
        dest.writeValue(title);
        dest.writeValue(video);
        dest.writeValue(voteAverage);
        dest.writeValue(voteCount);
    }

    public int describeContents() {
        return 0;
    }

}
