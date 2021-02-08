package com.example.phpandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    EditText name;
    Button send, read;
    List<Note> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.name);
        send = findViewById(R.id.send);
        read = findViewById(R.id.read);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getAppClient().create(ApiInterface.class);
                Call<Note> call = apiInterface.saveNote(name.getText().toString());

                call.enqueue(new Callback<Note>() {
                    @Override
                    public void onResponse(Call<Note> call, Response<Note> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Note> call, Throwable t) {

                    }
                });
            }
        });

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiInterface = ApiClient.getAppClient().create(ApiInterface.class);
                Call<List<Note>> call = apiInterface.getNotes();
                call.enqueue(new Callback<List<Note>>() {
                    @Override
                    public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            System.out.println(response.body());
                            users = response.body();
                            for (Note note : users) {
                                System.out.println("-----------------");
                                System.out.println(note.getId());
                                System.out.println(note.getName());
                                System.out.println("-----------------");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Note>> call, Throwable t) {

                    }
                });
            }
        });
    }
}