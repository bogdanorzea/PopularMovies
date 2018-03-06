package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

public class Review {

    @Json(name = "id")
    public String id;
    @Json(name = "author")
    public String author;
    @Json(name = "content")
    public String content;
    @Json(name = "url")
    public String url;

}