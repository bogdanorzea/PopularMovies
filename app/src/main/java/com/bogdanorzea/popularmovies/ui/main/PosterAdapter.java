package com.bogdanorzea.popularmovies.ui.main;

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
import com.bogdanorzea.popularmovies.ui.details.DetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.ViewHolder> {
    private final Context context;
    private List<Movie> mMovies;

    public PosterAdapter(Context context) {
        this.context = context;
        mMovies = new ArrayList<>();
    }

    public List<Movie> getMovies() {
        return mMovies;
    }

    public boolean isEmpty() {
        return mMovies.size() < 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View linearLayout = LayoutInflater.from(context).inflate(R.layout.item_poster, parent, false);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);

        holder.itemView.setTag(movie);

        Picasso.with(context)
                .load(movie.getPosterUrl())
                .error(R.drawable.missing_cover)
                .into(holder.coverImage);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void addMovies(@NonNull List<Movie> data) {
        int size = getItemCount();
        mMovies.addAll(data);
        if (size > 0) {
            notifyItemRangeInserted(size, data.size());
        } else {
            notifyDataSetChanged();
        }
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
