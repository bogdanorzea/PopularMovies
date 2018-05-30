package com.bogdanorzea.popularmovies.ui.details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bogdanorzea.popularmovies.R;
import com.bogdanorzea.popularmovies.model.object.Cast;
import com.bogdanorzea.popularmovies.model.response.CreditsResponse;
import com.bogdanorzea.popularmovies.utility.AsyncTaskUtils;
import com.bogdanorzea.popularmovies.utility.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import static com.bogdanorzea.popularmovies.utility.NetworkUtilsKt.hasInternetConnection;


public class CastTab extends Fragment {
    private static final String SAVED_LIST = "saved_list";
    private AVLoadingIndicatorView mAvi;
    private TextView warningTextView;
    private CastAdapter mAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_recycler_view, container, false);
        mAvi = view.findViewById(R.id.avi);
        warningTextView = view.findViewById(R.id.warning);

        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new CastAdapter(getActivity());

        mRecyclerView.setAdapter(mAdapter);

        if (savedInstanceState != null) {
            ArrayList<Cast> castList = savedInstanceState.getParcelableArrayList(SAVED_LIST);

            displayCredits(castList);
        } else {
            if (getArguments() != null) {
                int movieId = getArguments().getInt("movie_id");

                if (hasInternetConnection(getContext())) {
                    AsyncTaskUtils.RequestTaskListener<CreditsResponse> mRequestTaskListener =
                            new AsyncTaskUtils.RequestTaskListener<CreditsResponse>() {
                                @Override
                                public void onTaskStarting() {
                                    showProgress();
                                }

                                @Override
                                public void onTaskComplete(CreditsResponse result) {
                                    hideProgress();
                                    displayCredits(result.getCast());
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
        outState.putParcelableArrayList(SAVED_LIST, (ArrayList<Cast>) mAdapter.getCast());
    }

    private void displayCredits(List<Cast> list) {
        if (list.isEmpty()) {
            displayWarning(getString(R.string.warning_no_data));
        } else {
            mAdapter.addCast(list);
        }
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
}
