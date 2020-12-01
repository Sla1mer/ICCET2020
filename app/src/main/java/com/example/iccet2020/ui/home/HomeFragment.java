package com.example.iccet2020.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.iccet2020.R;
import com.example.iccet2020.ui.Changes.ChangesFragment;
import com.google.android.gms.dynamic.FragmentWrapper;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button changeBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        changeBtn = root.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            Fragment fragment = null;
            @Override
            public void onClick(View view) {

                    if(view.getId() == R.id.changeBtn){
                        fragment = new ChangesFragment();
                    }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                    ft.replace(R.id.navigation_home, fragment);
                    ft.commit();

            }
        });


        return root;
    }
}