package com.arctouch.codechallenge.ui.moviedetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.application.App;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.BlurTransformation;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.backdropImage)
    ImageView backdropImage;
    @BindView(R.id.titleTextView)
    TextView titleTextView;
    @BindView(R.id.genresTextView)
    TextView genresTextView;
    @BindView(R.id.overviewTextView)
    TextView overviewTextView;
    @BindView(R.id.releaseDateTextView)
    TextView releaseDateTextView;
    @BindView(R.id.posterImageView)
    ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        onIntentReceived();
        setBackArrow();
    }

    private void setBackArrow() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void onIntentReceived() {
        Movie movie = App.getGson().fromJson(getIntent().getStringExtra("movie"), Movie.class);
        if(movie != null){
            bindMovie(movie);
        }
    }

    private void bindMovie(Movie movie) {
        setTitle(movie.getTitle());
        Glide.with(this).load(new MovieImageUrlBuilder()
                .buildBackdropUrl(movie.getBackdropPath()))
                .apply(new RequestOptions().placeholder(R.drawable.movie_placeholder))
                .into(backdropImage);
        Glide.with(this).load(new MovieImageUrlBuilder().buildPosterUrl(movie.getPosterPath()))
                .apply(new RequestOptions().transforms( new BlurTransformation(10,3), new CenterCrop()))
                .into(posterImageView);

        titleTextView.setText(movie.getTitle());
        overviewTextView.setText(movie.getOverview());
        genresTextView.setText(movie.getGenres().toString()
                .replace("[", "").replace("]", ""));
        releaseDateTextView.setText("Release date: ".concat(movie.getReleaseDate()));

    }
}
