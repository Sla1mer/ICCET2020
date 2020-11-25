package com.example.iccet2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextInputEditText password;
    private Button signinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

       signinBtn.setOnClickListener(BTNs);
    }
    View.OnClickListener BTNs = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.registration){
                Intent intent = new Intent(getApplicationContext(), TakeNoteActivity.class);
                startActivity(intent);
            }
        }
    };

    // Инициализация элементов интерфейса
    private void init()
    {
        signinBtn = findViewById(R.id.registration);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
    }
}