package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.ui.DetailsActivity;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.ViewHolder> {
    private final Context context;
    private int nextPageToLoad = 1;
    private List<Movie> mMovies;

    public CoverAdapter(Context context, List<Movie> movies) {
        this.mMovies = movies;
        this.context = context;
    }

    public int getNextPageToLoad() {
        return nextPageToLoad;
    }

    public void setMovies(List<Movie> movies) {
        nextPageToLoad = 1;
        this.mMovies = movies;
        nextPageToLoad++;
    }

    public void addMovies(List<Movie> movies) {
        this.mMovies.addAll(movies);
        nextPageToLoad++;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View linearLayout = LayoutInflater.from(context).inflate(R.layout.item_poster, parent, false);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie currentMovie = mMovies.get(position);

        holder.itemView.setTag(currentMovie);
        NetworkUtils.loadImage(context, holder.coverImage, currentMovie.getPosterUrl());
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();

        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView coverImage;

        ViewHolder(View view) {
            super(view);

            coverImage = view.findViewById(R.id.poster);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Movie currentMovie = (Movie) itemView.getTag();

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(DetailsActivity.MOVIE_ID, currentMovie.id);
            context.startActivity(intent);
        }
    }

}
