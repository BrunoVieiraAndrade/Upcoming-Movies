package com.arctouch.codechallenge.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.presenter.MoviePresenter;
import com.arctouch.codechallenge.application.App;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.moviedetail.MovieDetailActivity;

import java.util.List;

public class HomeActivity extends BaseActivity implements MovieChoosingCallback, MoviePresenter.MoviesLoadingCallback {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    MoviePresenter moviePresenter;
    List<Movie> movies;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);
        moviePresenter = new MoviePresenter();
        moviePresenter.loadGenresAndMovies(this);
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        this.movies = movies;
        recyclerView.setAdapter(new HomeAdapter(this, movies));
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onMovieChoosed(Movie movie) {
        startActivity(new Intent(this, MovieDetailActivity.class)
        .putExtra("movie", App.getGson().toJson(movie)));
    }
}
