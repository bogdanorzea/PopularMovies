package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.model.object.TruncatedMovie;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.ViewHolder> {
    private final Context context;
    int nextPageToLoad = 1;
    List<TruncatedMovie> movies;
    private LayoutInflater inflater;

    CoverAdapter(Context context, List<TruncatedMovie> movies) {
        this.movies = movies;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View linearLayout = inflater.inflate(R.layout.movie_cover_layout, parent, false);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TruncatedMovie currentMovie = movies.get(position);

        holder.itemView.setTag(currentMovie);
        NetworkUtils.loadPoster(context, holder.coverImage, currentMovie.posterPath);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView coverImage;

        ViewHolder(View view) {
            super(view);

            coverImage = view.findViewById(R.id.cover_image);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            TruncatedMovie currentMovie = (TruncatedMovie) itemView.getTag();

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(DetailsActivity.MOVIE_ID_INTENT_KEY, currentMovie.id);
            context.startActivity(intent);
        }
    }

}
