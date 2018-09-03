package com.arctouch.codechallenge.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.presenter.MoviePresenter;
import com.arctouch.codechallenge.application.App;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.ui.moviedetail.MovieDetailActivity;
import com.arctouch.codechallenge.util.MovieFilter;

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
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                homeAdapter.setMovies(movies);
                return true;
            }
        });

        return true;
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
        if (movies.isEmpty()) {
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

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            homeAdapter.setMovies(new MovieFilter(movies).filter(query));
        }
    }
}
