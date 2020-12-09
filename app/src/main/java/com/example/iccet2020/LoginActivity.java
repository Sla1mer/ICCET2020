package com.example.iccet2020;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText password;
    private Button signinBtn;
    private FirebaseAuth mAuth;
    private static final String TAG = "mAuth";
    private Button textRegister, textForgetPassoword;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";
    private Shifr shifr = new Shifr();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();

       signinBtn.setOnClickListener(BTNs);
       textRegister.setOnClickListener(BTNs);
    }
    View.OnClickListener BTNs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.signin){
                signin(email.getText().toString(), password.getText().toString());
            }else if(v.getId() == R.id.textRegister){
                Intent intent = new Intent(getApplicationContext(), RegistraitActivity.class);
                startActivity(intent);
            }
        }
    };


    // Инициализация элементов интерфейса
    private void init()
    {

        textRegister = findViewById(R.id.textRegister);
        signinBtn = findViewById(R.id.signin);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
    }

    // Вход в аккаунт
    private void signin(String email, String password){
        try{
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            signIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    private void signIn(FirebaseUser firebaseUser)
    {

        try{
        if (firebaseUser != null){
            mDataBase.child("User").child(firebaseUser.getUid());
            System.out.println(firebaseUser.getUid());
            System.out.println(firebaseUser.getEmail());
            mDataBase.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(firebaseUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = new User();
                            user.setStatus(dataSnapshot.getValue(User.class).getStatus());

                            user.setStatus(shifr.dehifator(user.getStatus()));

                            if (user.getStatus().contains("patient"))
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            mDataBase.child("User").child(currentUser.getUid());
            System.out.println(currentUser.getUid());
            System.out.println(currentUser.getEmail());
            mDataBase.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user = new User();
                            user.setStatus(dataSnapshot.getValue(User.class).getStatus());

                            user.setStatus(shifr.dehifator(user.getStatus()));

                            if (user.getStatus().contains("patient"))
                            {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), DoctorActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}