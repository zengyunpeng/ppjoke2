package com.teemo.ppjoke2.ui.my;

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

@FragmentDestination(pageUrl = "main/tabs/my")
public class MyFragment extends Fragment {

    private MyViewModel mViewModel;

    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("tag","MyFragment==onCreateView");
        return inflater.inflate(R.layout.my_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MyViewModel.class);
        // TODO: Use the ViewModel
    }

}
