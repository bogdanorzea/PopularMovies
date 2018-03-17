package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.data.MovieMapper;
import com.bogdanorzea.popularmovies.model.object.Movie;
import com.bogdanorzea.popularmovies.ui.DetailsActivity;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;

public class PosterCursorAdapter extends RecyclerView.Adapter<PosterCursorAdapter.ViewHolder> {
    private final Context context;

    private Cursor mCursor;

    public PosterCursorAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View linearLayout = LayoutInflater.from(context).inflate(R.layout.item_poster, parent, false);

        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mCursor.moveToPosition(position)) {
            Movie currentMovie = MovieMapper.fromCursor(mCursor);

            holder.itemView.setTag(currentMovie);
            NetworkUtils.loadPoster(context, holder.coverImage, currentMovie.posterPath);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null)
            return mCursor.getCount();

        return 0;
    }

    public void swapData(Cursor data) {
        mCursor = data;
        notifyDataSetChanged();
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
            intent.putExtra(DetailsActivity.MOVIE_ID_INTENT_KEY, currentMovie.id);
            context.startActivity(intent);
        }
    }

}
