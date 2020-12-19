package com.example.iccet2020.ui.History;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iccet2020.R;
import com.example.iccet2020.Shifr;
import com.example.iccet2020.ZapicDoctor;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    DatabaseReference myRef2;
    FirebaseDatabase mFirebaseDatabase;
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private Shifr shifr = new Shifr();
    String data = "";

    public HistoryAdapter(Context ct, ArrayList<ZapicDoctor> arrayList, FirebaseDatabase firebaseDatabase, DatabaseReference databaseReference, String data){
        this.context = ct;
        this.arrayList = arrayList;
        myRef = databaseReference;
        mFirebaseDatabase = firebaseDatabase;
        this.data = data;
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
        System.out.println(arrayList + " ARRAYARRATY");
        holder.date.setText("Дата: " + shifr.dehifator(arrayList.get(position).getData()));
        holder.time.setText("Время: " + arrayList.get(position).getTime());
        holder.kab.setText("Кабинет: " + shifr.dehifator(arrayList.get(position).getKabinet()));
        holder.doctor.setText("Доктор: " + shifr.dehifator(arrayList.get(position).getDoctor()));
        StringBuilder sb = new StringBuilder(shifr.dehifator(arrayList.get(position).getDoctor()));
        String doctor = sb.delete(0, 1).toString().toLowerCase();

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = removePunct2(data);
                myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctor).child(date);
                myRef.orderByChild("time").equalTo(zapicDoctor.getTime()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot datas : snapshot.getChildren())
                            {
                                arrayList.clear();
                                String key = datas.getKey();
                                myRef = mFirebaseDatabase.getReference("User").child("Запись " + doctor).child(date)
                                        .child(key);
                                myRef.removeValue();

                                myRef2 = mFirebaseDatabase.getReference("User");
                                getData(date, myRef2);
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

    private void getData(String date, DatabaseReference myRef){
            arrayList.clear();
            myRef = mFirebaseDatabase.getReference("User").child("Запись терапевт").child(date);

            myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                            zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                            zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
                            zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                            zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
                            zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
                            zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
                            zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
                            zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
                            zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                            zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
                            zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
                            System.out.println(date);
                            System.out.println(zapicDoctor.getEmail());

                            arrayList.add(zapicDoctor);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            myRef = mFirebaseDatabase.getReference("User").child("Запись хирург").child(date);

            myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                            zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                            zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
                            zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                            zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
                            zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
                            zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
                            zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
                            zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
                            zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                            zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
                            zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
                            System.out.println(date);
                            System.out.println(zapicDoctor.getEmail());

                            arrayList.add(zapicDoctor);
                            notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            myRef = mFirebaseDatabase.getReference("User").child("Запись отоларинголог").child(date);

            myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            ZapicDoctor zapicDoctor = new ZapicDoctor();
                            zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
                            zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
                            zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
                            zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
                            zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
                            zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
                            zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
                            zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
                            zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
                            zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
                            zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
                            zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
                            zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
                            zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
                            zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
                            System.out.println(date);
                            System.out.println(zapicDoctor.getEmail());

                            arrayList.add(zapicDoctor);
                            notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

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
