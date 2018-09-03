package com.arctouch.codechallenge.ui.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static final int LOADING = 1;
    private static final int ITEM = 0;
    private List<Movie> movies;
    private MovieChoosingCallback movieChoosingCallback;
    private boolean isLoadingAdded = false;

    public HomeAdapter(MovieChoosingCallback callback) {
        movies = new ArrayList<>();
        this.movieChoosingCallback = callback;
    }

    public void addMovies(List<Movie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

        private final TextView titleTextView;
        private final TextView genresTextView;
        private final TextView releaseDateTextView;
        private final ImageView posterImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            genresTextView = itemView.findViewById(R.id.genresTextView);
            releaseDateTextView = itemView.findViewById(R.id.releaseDateTextView);
            posterImageView = itemView.findViewById(R.id.posterImageView);
            itemView.setOnClickListener(view -> {
                movieChoosingCallback.onMovieChoosed(movies.get(getAdapterPosition()));
            });
        }

        public void bind(Movie movie) {
            titleTextView.setText(movie.title);
            genresTextView.setText(TextUtils.join(", ", movie.genres));
            releaseDateTextView.setText(movie.releaseDate);

            String posterPath = movie.posterPath;
            if (TextUtils.isEmpty(posterPath) == false) {
                Glide.with(itemView)
                        .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                        .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                        .into(posterImageView);
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return movies == null ? 0 : movies.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(movies.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return (position == movies.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void add(Movie movie){
        movies.add(movie);
        notifyItemInserted(movies.size() - 1);
    }

    public void addAll(List<Movie> movies){
        for (Movie movie : movies) {
            add(movie);
        }
    }

    public void remove(Movie movie){
        int position = movies.indexOf(movie);
        if (position > -1) {
            movies.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Movie());
    }

        public void removeLoadingFooter() {
            isLoadingAdded = false;

            int position = movies.size() - 1;
            Movie item = getItem(position);

            if (item != null) {
                movies.remove(position);
                notifyItemRemoved(position);
            }
    }

    public Movie getItem(int position) {
        return movies.get(position);
    }
}
