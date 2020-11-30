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
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

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
                dateMSG = removePunct2(date.getText().toString());

                mDataBase.child(nameMSG).child(dateMSG).child(timeMSG).setValue(shedule);
            }
        });
    }

    public static String removePunct2(String str) {
        StringBuilder result = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (PUNCT.indexOf(c) < 0) {
                result.append(c);
            }
        }
        return result.toString();
    }
}