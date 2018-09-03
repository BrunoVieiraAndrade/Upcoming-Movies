package com.arctouch.codechallenge.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.presenter.MoviePresenter;
import com.arctouch.codechallenge.application.App;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.moviedetail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements MovieChoosingCallback, MoviePresenter.MoviesLoadingCallback {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    MoviePresenter moviePresenter;
    HomeAdapter homeAdapter;
    List<Movie> movies;
    boolean noMorePages;
    int currentPage = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        moviePresenter = new MoviePresenter();
        homeAdapter = new HomeAdapter(this);
        initializeList();
        moviePresenter.loadGenresAndMovies(this);
    }

    private void initializeList() {
        movies = new ArrayList<>();
        recyclerView.setAdapter(homeAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && !noMorePages) {
                    moviePresenter.loadGenresAndMovies(HomeActivity.this, currentPage + 1);
                    currentPage++;
                }
            }
        });
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        if(movies.isEmpty()){
            noMorePages = true;
            return;
        }
        this.movies = movies;
        homeAdapter.addMovies(movies);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMovieChoosed(Movie movie) {
        startActivity(new Intent(this, MovieDetailActivity.class)
        .putExtra("movie", App.getGson().toJson(movie)));
    }
}
