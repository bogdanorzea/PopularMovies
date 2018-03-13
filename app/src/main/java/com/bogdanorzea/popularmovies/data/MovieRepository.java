package com.bogdanorzea.popularmovies.data;

import java.util.List;

public interface MovieRepository<T> {
    void insert(T movie);

    void update(T movie);

    void setFavorite(int movieId, boolean isFavorite);

    boolean isFavorite(int movieId);

    void delete(int movieId);

    List<T> getAll();

    T get(int movieId);
}
