package com.bogdanorzea.popularmovies.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.adapter.CastAdapter;
import com.bogdanorzea.popularmovies.model.object.Cast;
import com.bogdanorzea.popularmovies.model.response.CreditsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;


public class CastTab extends Fragment {
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private CastAdapter mCastAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_list_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        if (savedInstanceState != null) {
            ArrayList<Cast> castList = savedInstanceState.getParcelableArrayList("cast_list");

            displayCredits(castList);
        } else {
            if (getArguments() != null) {
                int movieId = getArguments().getInt("movie_id");

                if (NetworkUtils.hasInternetConnection(getContext())) {
                    AsyncTaskUtils.RequestTaskListener<CreditsResponse> mRequestTaskListener =
                            new AsyncTaskUtils.RequestTaskListener<CreditsResponse>() {
                                @Override
                                public void onTaskStarting() {
                                    showProgress();
                                }

                                @Override
                                public void onTaskComplete(CreditsResponse result) {
                                    hideProgress();
                                    displayCredits(result.cast);
                                }
                            };

                    new AsyncTaskUtils.RequestTask<>(mRequestTaskListener, CreditsResponse.class)
                            .execute(NetworkUtils.movieCreditsUrl(movieId));
                } else {
                    displayWarning(getString(R.string.warning_no_internet));
                }
            }
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("cast_list", (ArrayList<Cast>) mCastAdapter.getCast());
    }

    private void displayCredits(List<Cast> castList) {
        View view = getView();
        if (view == null) {
            return;
        }

        ListView listView = view.findViewById(R.id.list);
        ViewCompat.setNestedScrollingEnabled(listView, true);

        if (castList.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
        }

        mCastAdapter = new CastAdapter(getActivity(), castList);

        listView.setAdapter(mCastAdapter);
        mCastAdapter.notifyDataSetChanged();
    }

    private void showProgress() {
        mAvi.smoothToShow();
    }

    private void hideProgress() {
        mAvi.smoothToHide();
    }

    private void displayWarning(String message) {
        warningTextView.setVisibility(View.VISIBLE);
        warningTextView.setText(message);
    }

    private void hideWarning() {
        warningTextView.setVisibility(View.GONE);
    }
}
