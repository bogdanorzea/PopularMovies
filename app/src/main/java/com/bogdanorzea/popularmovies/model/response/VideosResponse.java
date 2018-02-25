package com.bogdanorzea.popularmovies.model.response;

import com.bogdanorzea.popularmovies.model.objects.Video;
import com.squareup.moshi.Json;

import java.util.List;

public class VideosResponse {

    @Json(name = "id")
    public int id;
    @Json(name = "results")
    public List<Video> results = null;
}
