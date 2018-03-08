package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class ProductionCountry implements Parcelable {

    public final static Parcelable.Creator<ProductionCountry> CREATOR = new Creator<ProductionCountry>() {

        public ProductionCountry createFromParcel(Parcel in) {
            return new ProductionCountry(in);
        }

        public ProductionCountry[] newArray(int size) {
            return (new ProductionCountry[size]);
        }

    };
    @Json(name = "iso_3166_1")
    public String iso31661;
    @Json(name = "name")
    public String name;

    protected ProductionCountry(Parcel in) {
        this.iso31661 = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProductionCountry() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(iso31661);
        dest.writeValue(name);
    }

    public int describeContents() {
        return 0;
    }

}