package com.bogdanorzea.popularmovies.data;


interface Mapper<From, To> {
    To map(From from);
}
