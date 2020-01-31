package com.teemo.ppjoke2.ui.publish;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooc.libnavannotation.FragmentDestination;
import com.teemo.ppjoke2.R;
@FragmentDestination(pageUrl = "main/tabs/publish")
public class PublishFragment extends Fragment {

    private PublishViewModel mViewModel;

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("tag","PublishFragment==onCreateView");
        return inflater.inflate(R.layout.publish_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PublishViewModel.class);
        // TODO: Use the ViewModel
    }

}
