package com.bogdanorzea.popularmovies.data;

import java.util.List;

public interface RepositoryMovie<T> {
    void insert(T movie);

    void update(T movie);

    void setFavorite(int movieId, boolean isFavorite);

    boolean isFavorite(int movieId);

    void delete(int movieId);

    void deleteAll();

    List<T> getAll();

    List<T> getFavorites(String sortOrder);

    T get(int movieId);
}

