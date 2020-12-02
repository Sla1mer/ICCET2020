package com.example.iccet2020.ui.home;

import android.content.Intent;
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

import com.example.iccet2020.ChangesActivity;
import com.example.iccet2020.MainActivity;
import com.example.iccet2020.R;


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
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.changeBtn){
                    Intent intent = new Intent(getContext(), ChangesActivity.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }
}