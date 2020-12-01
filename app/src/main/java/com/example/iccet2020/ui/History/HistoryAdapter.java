package com.example.iccet2020.ui.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iccet2020.R;
import com.example.iccet2020.ZapicDoctor;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<ZapicDoctor> arrayList;
    DatabaseReference myRef;
    FirebaseDatabase mFirebaseDatabase;
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    public HistoryAdapter(Context ct, ArrayList<ZapicDoctor> arrayList, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference){
        this.context = ct;
        this.arrayList = arrayList;
        myRef = databaseReference;
        mFirebaseDatabase = firebaseDatabase;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row2, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ZapicDoctor zapicDoctor = arrayList.get(position);
        holder.date.setText(arrayList.get(position).getData());
        holder.time.setText(arrayList.get(position).getTime());
        holder.kab.setText(arrayList.get(position).getKabinet());
        holder.doctor.setText(arrayList.get(position).getDoctor());

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = removePunct2(zapicDoctor.getData());
                myRef = mFirebaseDatabase.getReference("User").child("Запись " + zapicDoctor.getDoctor().toLowerCase()).child(date);
                myRef.orderByChild("time").equalTo(zapicDoctor.getTime()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot datas : snapshot.getChildren())
                            {
                                String key = datas.getKey();
                                myRef = mFirebaseDatabase.getReference("User").child("Запись " + zapicDoctor.getDoctor().toLowerCase()).child(date)
                                        .child(key);
                                myRef.removeValue();
                                arrayList.remove(arrayList.get(position));
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        MaterialTextView date, time, kab, doctor;
        ConstraintLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            kab = itemView.findViewById(R.id.kab);
            doctor = itemView.findViewById(R.id.doctor);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }
}
