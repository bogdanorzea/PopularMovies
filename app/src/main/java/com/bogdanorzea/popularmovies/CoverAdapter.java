package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.model.Movie;
import com.bogdanorzea.popularmovies.utils.NetworkUtils;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.ViewHolder> {
    private final Context context;
    private List<Movie> movies;
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

        holder.itemView.setTag(currentMovie.id);
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

            int id = (int) itemView.getTag();
            Intent intent = new Intent(context, DetailsActivity.class);
            // TODO extract hardcoded key
            intent.putExtra("movie_id", id);
            context.startActivity(intent);
        }
    }

}
