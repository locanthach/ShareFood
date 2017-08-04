package com.locanthach.sharefood.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.locanthach.sharefood.R;
import com.locanthach.sharefood.model.Post;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by nongdenchet on 10/28/16.
 */

public class MyDialogFragment extends BottomSheetDialogFragment {

    private static final String POST = "POST";
    private Post post;

    @BindView(R.id.rvComments)
    RecyclerView rvComments;

    public static MyDialogFragment newInstance(Post post) {

        Bundle args = new Bundle();
        args.putParcelable(POST, post);
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        post = getArguments().getParcelable(POST);
        //TODO: display list comments here;
    }
}
