package com.arctouch.codechallenge.util;

import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieFilter {
    String query;
    private List<Movie> movies;
    private List<Movie> filteredMovies;
    public MovieFilter(List<Movie> movies) {
        this.movies = movies;
        filteredMovies = new ArrayList<>();
    }

    public List<Movie> filter(String query) {
        for (Movie movie: movies) {
            if(movie.getTitle().toUpperCase().contains(query.toUpperCase())){
                filteredMovies.add(movie);
            }
        }
        return filteredMovies;
    }
}
