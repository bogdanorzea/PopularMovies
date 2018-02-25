package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.model.objects.Movie;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.ViewHolder> {
    int nextPageToLoad = 1;
    List<Movie> movies;
    private final Context context;
    private LayoutInflater inflater;

    CoverAdapter(Context context, List<Movie> movies) {
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
        Movie currentMovie = movies.get(position);

        holder.itemView.setTag(currentMovie);
        NetworkUtils.loadImage(context, holder.coverImage, currentMovie.posterPath);
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

            Movie currentMovie = (Movie) itemView.getTag();

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(DetailsActivity.MOVIE_TITLE_INTENT_KEY, currentMovie.title);
            intent.putExtra(DetailsActivity.MOVIE_ID_INTENT_KEY, currentMovie.id);
            context.startActivity(intent);
        }
    }

}
