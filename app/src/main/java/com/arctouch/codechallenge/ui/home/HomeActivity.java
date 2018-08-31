package com.arctouch.codechallenge.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.application.App;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.moviedetail.MovieDetailActivity;
import com.arctouch.codechallenge.util.recyclerview.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity implements MovieChoosingCallback {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;
    HomeAdapter adapter;
    List<Movie> movies;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        initializeMoviesList();
    }

    private void loadMovies() {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> onMoviesLoaded(response.results));
    }

    private void onMoviesLoaded(List<Movie> movies) {
        for (Movie movie : movies) {
            movie.genres = new ArrayList<>();
            for (Genre genre : Cache.getGenres()) {
                if (movie.genreIds.contains(genre.id)) {
                    movie.genres.add(genre);
                }
            }
        }
        this.movies = movies;
        adapter.addAll(movies);
        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void initializeMoviesList() {
        adapter = new HomeAdapter(this, movies);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressBar.setVisibility(View.GONE);
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1; //Increment page index to load the next one
//                loadNextPage();
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        loadFirstPage();
    }

    private void loadFirstPage() {
       loadGenresAndMovies();
    }

    private void loadGenresAndMovies() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    loadMovies();
                });
    }

    @Override
    public void onMovieChoosed(Movie movie) {
        startActivity(new Intent(this, MovieDetailActivity.class)
        .putExtra("movie", App.getGson().toJson(movie)));
    }
}
