package com.example.iccet2020;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mAuth = FirebaseAuth.getInstance();
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            Intent intent = new Intent(getApplicationContext(), TakeNoteActivity.class);
            startActivity(intent);
        }
    }

//<<<<<<< HEAD
//    View.OnClickListener BTNs = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()){
//                case (R.id.registration):
//                    Intent intent_For_registration = new Intent(getApplicationContext(), RegistraitActivity.class);
//                    startActivity(intent_For_registration);
//                    break;
//                case (R.id.sign_in):
//                    Intent intent_for_signIn = new Intent(getApplicationContext(), LoginActivity.class);
//                    startActivity(intent_for_signIn);
//                    break;
//
//            }
//        }
//    };
//
//    // Инициализация элементов интерфейса
//    private void init()
//    {
//        signinBnt = findViewById(R.id.sign_in);
//        registrationBtn = findViewById(R.id.registration);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
//            startActivity(intent);
//        }
//    }
//=======
//>>>>>>> b23aba14c5b89a4334b820400da3b74869780312
}