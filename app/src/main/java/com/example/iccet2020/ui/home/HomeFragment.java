package com.example.iccet2020.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;

import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.iccet2020.ChangesActivity;
import com.example.iccet2020.LoginActivity;
import com.example.iccet2020.MainActivity;
import com.example.iccet2020.R;
import com.example.iccet2020.Shifr;
import com.example.iccet2020.User;
import com.example.iccet2020.ZapicDoctor;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button changeBtn;

    private MaterialTextView nameOfPerson, data, snils, email, phone, polisSeria, polisNumber;
    private FirebaseAuth mAuth;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private DatabaseReference myRef;
    private FirebaseDatabase mFirebaseDatabase;
    private Shifr shifr = new Shifr();

    private Button exit, refresh;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        changeBtn = root.findViewById(R.id.changeBtn);

        nameOfPerson = root.findViewById(R.id.nameOfPerson);
        data = root.findViewById(R.id.dataOfBirthday);
        snils = root.findViewById(R.id.snils);
        email = root.findViewById(R.id.email);
        phone = root.findViewById(R.id.phoneNumber);
        polisSeria = root.findViewById(R.id.polisSeria);
        polisNumber = root.findViewById(R.id.polisNumber);

        exit = root.findViewById(R.id.exit);
        refresh = root.findViewById(R.id.reload);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });


        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.changeBtn){
                    Intent intent = new Intent(getContext(), ChangesActivity.class);
                    startActivity(intent);
                }
            }
        });


        getData();


        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.exit){

                    //также как и выше
                    mAuth.signOut();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });



        return root;
    }

    private void getData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDataBase.child("User").child(currentUser.getUid());
        System.out.println(currentUser.getUid());
        mDataBase.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = new User();
                        user.setFirstname(dataSnapshot.getValue(User.class).getFirstname());
                        user.setLastname(dataSnapshot.getValue(User.class).getLastname());
                        user.setMiddlename(dataSnapshot.getValue(User.class).getMiddlename());

                        user.setFirstname(shifr.dehifator(user.getFirstname()));
                        user.setLastname(shifr.dehifator(user.getLastname()));
                        user.setMiddlename(shifr.dehifator(user.getMiddlename()));

                        String fio = user.getLastname() + " " + user.getFirstname() + " " + user.getMiddlename();
                        user.setBirhday(dataSnapshot.getValue(User.class).getBirhday());
                        user.setSnils(dataSnapshot.getValue(User.class).getSnils());
                        user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                        user.setPhone(dataSnapshot.getValue(User.class).getPhone());
                        user.setSeriaOMS(dataSnapshot.getValue(User.class).getSeriaOMS());
                        user.setNomerOMS(dataSnapshot.getValue(User.class).getNomerOMS());

                        user.setBirhday(shifr.dehifator(user.getBirhday()));
                        user.setSnils(shifr.dehifator(user.getSnils()));
                        user.setEmail(shifr.dehifatorEmail(user.getEmail()));
                        user.setPhone(shifr.dehifator(user.getPhone()));
                        user.setSeriaOMS(shifr.dehifator(user.getSeriaOMS()));
                        user.setNomerOMS(shifr.dehifator(user.getNomerOMS()));

                        System.out.println(fio);
                        System.out.println(user.getBirhday());

                        nameOfPerson.setText(fio);
                        data.setText(user.getBirhday());
                        snils.setText(user.getSnils());
                        email.setText(user.getEmail());
                        phone.setText(user.getPhone());
                        polisSeria.setText(user.getSeriaOMS());
                        polisNumber.setText(user.getNomerOMS());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}