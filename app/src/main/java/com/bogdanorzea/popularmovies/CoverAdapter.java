package com.bogdanorzea.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.ViewHolder> {
    private final Context context;
    private List<Movie> movies;
    private LayoutInflater inflater;

    public CoverAdapter(Context context, List<Movie> movies) {
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
        ImageView imageView = holder.coverImage;
        holder.itemView.setTag(movies.get(position));

        // TODO remove hardcoded path
        Picasso.with(context).load("https://image.tmdb.org/t/p/w500" + movies.get(position).posterPath).into(imageView);
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
            String message = ((Movie) itemView.getTag()).title;
            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

}
