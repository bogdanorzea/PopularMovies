package com.bogdanorzea.popularmovies.model.object;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class ProductionCompany implements Parcelable {

    public final static Parcelable.Creator<ProductionCompany> CREATOR = new Creator<ProductionCompany>() {

        public ProductionCompany createFromParcel(Parcel in) {
            return new ProductionCompany(in);
        }

        public ProductionCompany[] newArray(int size) {
            return (new ProductionCompany[size]);
        }

    };
    @Json(name = "name")
    public String name;
    @Json(name = "id")
    public int id;

    protected ProductionCompany(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((int) in.readValue((int.class.getClassLoader())));
    }

    public ProductionCompany() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}