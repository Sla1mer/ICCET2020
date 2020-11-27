package com.example.iccet2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddShedule extends AppCompatActivity {

    private TextInputEditText date, time, name;
    private MaterialButton send;
    private DatabaseReference mDataBase;
    private String USER_KEY = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shedule);

        date = findViewById(R.id.date);
        time = findViewById(R.id.time);
        send = findViewById(R.id.send);
        name = findViewById(R.id.name);
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dateMSG = date.getText().toString();
                String timeMSG = time.getText().toString();
                String nameMSG = name.getText().toString();
                Shedule shedule = new Shedule(dateMSG, timeMSG);

                mDataBase.child(nameMSG).child(timeMSG).setValue(shedule);
            }
        });
    }
}