package com.bogdanorzea.popularmovies.ui.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Cast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {

    private Context context;
    private List<Cast> mCast;

    public CastAdapter(@NonNull Context context) {
        this.context = context;
        this.mCast = new ArrayList<>();
    }

    public List<Cast> getCast() {
        return mCast;
    }

    public void addCast(@NonNull List<Cast> data) {
        int size = getItemCount();
        mCast.addAll(data);
        if (size > 0) {
            notifyItemRangeInserted(size, data.size());
        } else {
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_cast, parent, false);

        return new CastAdapter.ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cast castMember = mCast.get(position);

        holder.name.setText(castMember.name);
        holder.description.setText(castMember.character);

        Picasso.with(context)
                .load(castMember.getProfileUrl())
                .error(R.drawable.missing_cover)
                .into(holder.profile);
    }

    @Override
    public int getItemCount() {
        return mCast.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView name;
        final TextView description;
        final ImageView profile;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            description = view.findViewById(R.id.character);
            profile = view.findViewById(R.id.profile);
        }

    }

}
