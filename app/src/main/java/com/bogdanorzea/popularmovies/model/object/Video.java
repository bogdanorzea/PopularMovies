package com.bogdanorzea.popularmovies.model.object;

import com.squareup.moshi.Json;

public class Video {
    private static final String YOUTUBE_DEFAULT_HQ_JPG_URL = "https://img.youtube.com/vi/%s/hqdefault.jpg";

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

    public String getYoutubeThumbnailLink() {
        return String.format(YOUTUBE_DEFAULT_HQ_JPG_URL, key);
    }

}
