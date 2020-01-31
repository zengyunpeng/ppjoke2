package com.teemo.ppjoke2.ui.find;

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

@FragmentDestination(pageUrl = "main/tabs/find")
public class FindFragment extends Fragment {

    private FindViewModel mViewModel;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("tag","FindFragment==onCreateView");

        return inflater.inflate(R.layout.find_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FindViewModel.class);
        // TODO: Use the ViewModel
    }

}
