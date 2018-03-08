package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.model.object.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends ArrayAdapter<Review> {

    List<Review> mReviews;

    public ReviewsAdapter(@NonNull Context context, List<Review> reviews) {
        super(context, 0, reviews);
        this.mReviews = reviews;
    }

    @Override
    public int getCount() {
        return mReviews.size();
    }

    @Override
    public Review getItem(int i) {
        return mReviews.get(i);
    }

    @NonNull
    @Override
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_2, viewGroup, false);
        }

        Review currentReview = mReviews.get(i);
        TextView author = view.findViewById(android.R.id.text1);
        TextView description = view.findViewById(android.R.id.text2);

        author.setText(currentReview.author);
        description.setText(currentReview.content);

        return view;
    }
}
