package com.teemo.ppjoke2.ui.sofa;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDestination;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mooc.libnavannotation.FragmentDestination;
import com.teemo.ppjoke2.R;
@FragmentDestination(pageUrl = "main/tabs/sofa")
public class SoFaFragment extends Fragment {

    private SoFaViewModel mViewModel;

    public static SoFaFragment newInstance() {
        return new SoFaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("tag","SoFaFragment==onCreateView");

        return inflater.inflate(R.layout.so_fa_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SoFaViewModel.class);
        // TODO: Use the ViewModel
    }

}
