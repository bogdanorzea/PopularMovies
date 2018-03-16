package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Review;

import java.util.List;

public class ReviewsAdapter extends ArrayAdapter<Review> {

    private List<Review> mReviews;

    public ReviewsAdapter(@NonNull Context context, List<Review> reviews) {
        super(context, 0, reviews);
        this.mReviews = reviews;
    }

    @Override
    public int getCount() {
        if (mReviews == null) {
            return 0;
        }

        return mReviews.size();
    }

    @Override
    public Review getItem(int i) {
        if (mReviews == null) {
            return null;
        }

        return mReviews.get(i);
    }

    @NonNull
    @Override
    public View getView(int i, View view, @NonNull ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_review, viewGroup, false);
        }

        Review currentReview = mReviews.get(i);
        TextView author = view.findViewById(R.id.author);
        TextView description = view.findViewById(R.id.content);

        author.setText(currentReview.author);
        description.setText(currentReview.content);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                if (!TextUtils.isEmpty(currentReview.url)) {
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(context, Uri.parse(currentReview.url));
                } else {
                    Toast.makeText(context, "Couldn't find the review url", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
