package com.bogdanorzea.popularmovies.data;


public interface Mapper<From, To> {
    To map(From from);
}
