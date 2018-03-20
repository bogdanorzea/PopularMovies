package com.bogdanorzea.popularmovies.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private Context context;
    private List<Review> mReviews;

    public ReviewsAdapter(@NonNull Context context) {
        this.context = context;
        this.mReviews = new ArrayList<>();
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void addReviews(@NonNull List<Review> data) {
        int size = getItemCount();
        mReviews.addAll(data);
        if (size > 0) {
            notifyItemRangeInserted(size, data.size());
        } else {
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);

        return new ReviewsAdapter.ViewHolder(layout);
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ViewHolder holder, int position) {
        Review currentReview = mReviews.get(position);

        holder.itemView.setTag(currentReview);

        holder.author.setText(currentReview.author);
        holder.description.setText(currentReview.content);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView author;
        final TextView description;

        public ViewHolder(View view) {
            super(view);

            author = view.findViewById(R.id.author);
            description = view.findViewById(R.id.content);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();

            Review review = (Review) itemView.getTag();

            if (!TextUtils.isEmpty(review.url)) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(review.url));
            } else {
                Toast.makeText(context, "Couldn't find the review url", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
