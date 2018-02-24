package com.bogdanorzea.popularmovies.model.objects;

import com.squareup.moshi.Json;

public class Video {

    @Json(name = "id")
    public String id;
    @Json(name = "iso_639_1")
    public String iso6391;
    @Json(name = "iso_3166_1")
    public String iso31661;
    @Json(name = "key")
    public String key;
    @Json(name = "name")
    public String name;
    @Json(name = "site")
    public String site;
    @Json(name = "size")
    public int size;
    @Json(name = "type")
    public String type;
}
