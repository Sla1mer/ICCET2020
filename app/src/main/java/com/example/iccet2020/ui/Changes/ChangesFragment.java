package com.example.iccet2020.ui.Changes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.iccet2020.R;
import com.example.iccet2020.ui.home.HomeFragment;

public class ChangesFragment extends Fragment {
private Button changeBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

        changeBtn = root.findViewById(R.id.changeBtn);
        changeBtn.setOnClickListener(new View.OnClickListener() {
            Fragment fragment = null;
            @Override
            public void onClick(View view) {

                if(view.getId() == R.id.changeBtn){
                    fragment = new HomeFragment();
                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.changeFr, fragment);
                ft.commit();

            }
        });

        return root;
    }
}