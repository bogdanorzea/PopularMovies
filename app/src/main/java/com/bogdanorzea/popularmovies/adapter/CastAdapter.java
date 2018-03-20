package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Cast;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CastAdapter extends ArrayAdapter<Cast> {

    private List<Cast> mCast;

    public CastAdapter(@NonNull Context context, List<Cast> cast) {
        super(context, 0, cast);
        this.mCast = cast;
    }

    public List<Cast> getCast() {
        return mCast;
    }

    @Override
    public int getCount() {
        if (mCast == null) {
            return 0;
        }

        return mCast.size();
    }

    @Override
    public Cast getItem(int i) {
        if (mCast == null) {
            return null;
        }

        return mCast.get(i);
    }

    @NonNull
    @Override
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_cast, viewGroup, false);
        }

        Cast castMember = mCast.get(i);
        TextView name = view.findViewById(R.id.name);
        name.setText(castMember.name);

        TextView description = view.findViewById(R.id.character);
        description.setText(castMember.character);

        ImageView profile = view.findViewById(R.id.profile);
        Picasso.with(getContext())
                .load(castMember.getProfileUrl())
                .error(R.drawable.missing_cover)
                .into(profile);

        return view;
    }

    public void addCast(@NonNull List<Cast> data) {
        int size = getCount();
        mCast.addAll(data);

        notifyDataSetChanged();

    }
}
