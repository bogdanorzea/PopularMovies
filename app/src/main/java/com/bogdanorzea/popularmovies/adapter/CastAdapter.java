package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.object.Cast;

import java.util.List;

public class CastAdapter extends ArrayAdapter<Cast> {

    private List<Cast> mCast;

    public CastAdapter(@NonNull Context context, List<Cast> cast) {
        super(context, 0, cast);
        this.mCast = cast;
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
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        Cast castMember = mCast.get(i);
        TextView author = view.findViewById(android.R.id.text1);
        TextView description = view.findViewById(android.R.id.text2);

        author.setText(castMember.name);
        description.setText(castMember.character);

        return view;
    }
}
