package com.example.iccet2020.ui.History;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import androidx.lifecycle.Observer;
        import androidx.lifecycle.ViewModelProviders;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.example.iccet2020.MyAdapter;
        import com.example.iccet2020.R;
import com.example.iccet2020.Shifr;
import com.example.iccet2020.TakeNoteActivity;
        import com.example.iccet2020.ZapicDoctor;
        import com.google.android.material.button.MaterialButton;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
        import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.iccet2020.TakeNoteActivity.removePunct2;

public class HistoryFragment extends Fragment {

    private HistoryViewModel notificationsViewModel;
    private RecyclerView recyclerView;
    private ArrayList<ZapicDoctor> arrayList;
    private DatabaseReference myRef;
    private String USER_KEY = "User";
    private FirebaseDatabase mFirebaseDatabase;
    private HistoryAdapter historyAdapter;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String date = "";
    private MaterialButton materialButton;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private Timer timer = new Timer();
    private boolean flag = false;
    private Shifr shifr = new Shifr();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        notificationsViewModel =
                ViewModelProviders.of(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        myRef = FirebaseDatabase.getInstance().getReference(USER_KEY);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        materialButton = root.findViewById(R.id.takeDate);

//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (flag){
//                    getData(date, arrayList);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            historyAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        }, 0, 1000);

        recyclerView = root.findViewById(R.id.recyclerHistory);
        arrayList = new ArrayList<>();

//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                if (flag)
//                {
//                    getData(date);
//                }
//            }
//        }, 0, 1000);

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "." + month + "." + year;
                String m = String.valueOf(month);
                String d = String.valueOf(day);
                System.out.println("MONTH " + month);
                if (String.valueOf(day).length() == 1)
                {
                    d = ("0" + day);
                }

                if (String.valueOf(month).length() == 1)
                {
                    m = ("0" + month);
                }

                date = d + "." + m + "." + year;
                date = removePunct2(date);
                ArrayList<ZapicDoctor> ar = new ArrayList<>();
                getData(date);
//                if (flag)
//                {
//                    historyAdapter = new HistoryAdapter(getContext(), arrayList, mFirebaseDatabase, myRef, date);
//                    recyclerView.setAdapter(historyAdapter);
//                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    System.out.println(ar + " qwertyuiop[;lkjhgfdsazxcvbnm,.");
//                }
            }
        };

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                        android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        return root;
    }

    private void getData(String date)
    {
        arrayList.clear();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        myRef = mFirebaseDatabase.getReference("User").child("Запись терапевт").child(date);

        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
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
                        historyAdapter = new HistoryAdapter(getContext(), arrayList, mFirebaseDatabase, myRef, date);
                        recyclerView.setAdapter(historyAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef = mFirebaseDatabase.getReference("User").child("Запись хирург").child(date);

        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
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
                        historyAdapter = new HistoryAdapter(getContext(), arrayList, mFirebaseDatabase, myRef, date);
                        recyclerView.setAdapter(historyAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef = mFirebaseDatabase.getReference("User").child("Запись отоларинголог").child(date);

        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
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
                        historyAdapter = new HistoryAdapter(getContext(), arrayList, mFirebaseDatabase, myRef, date);
                        recyclerView.setAdapter(historyAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myRef = mFirebaseDatabase.getReference("User").child("Запись отоларинголог").child(date);

        flag = true;

        System.out.println("ARRAYLIST " + arrayList);

//        arrayList2.clear();
//
//        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ZapicDoctor zapicDoctor = new ZapicDoctor();
//                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
//                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
//                    zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
//                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
//                    zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
//                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
//                    zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
//                    zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
//                    zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
//                    zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
//                    zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
//                    zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
//                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
//                    zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
//                    zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
//                    System.out.println(date);
//                    System.out.println(zapicDoctor.getEmail());
//
//                    arrayList2.add(zapicDoctor);
//                    System.out.println(arrayList2);
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    historyAdapter.notifyDataSetChanged();
//                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        myRef = mFirebaseDatabase.getReference("User").child("Запись хирург").child(date);
//
//        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ZapicDoctor zapicDoctor = new ZapicDoctor();
//                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
//                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
//                    zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
//                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
//                    zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
//                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
//                    zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
//                    zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
//                    zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
//                    zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
//                    zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
//                    zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
//                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
//                    zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
//                    zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
//                    System.out.println(date);
//                    System.out.println(zapicDoctor.getEmail());
//
//                    FirebaseUser currentUser = mAuth.getCurrentUser();
//                    arrayList2.add(zapicDoctor);
//                    System.out.println(arrayList2);
//                    historyAdapter.notifyDataSetChanged();
//                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        myRef = mFirebaseDatabase.getReference("User").child("Запись отоларинголог").child(date);
//        System.out.println(date + " DATE");
//
//        myRef.orderByChild("email").equalTo(shifr.hifr_zezaryaEmail(currentUser.getEmail())).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    ZapicDoctor zapicDoctor = new ZapicDoctor();
//                    zapicDoctor.setTime(dataSnapshot.getValue(ZapicDoctor.class).getTime());
//                    zapicDoctor.setData(dataSnapshot.getValue(ZapicDoctor.class).getData());
//                    zapicDoctor.setBirthday(dataSnapshot.getValue(ZapicDoctor.class).getBirthday());
//                    zapicDoctor.setDoctor(dataSnapshot.getValue(ZapicDoctor.class).getDoctor());
//                    zapicDoctor.setEmail(dataSnapshot.getValue(ZapicDoctor.class).getEmail());
//                    zapicDoctor.setKabinet(dataSnapshot.getValue(ZapicDoctor.class).getKabinet());
//                    zapicDoctor.setLastname(dataSnapshot.getValue(ZapicDoctor.class).getLastname());
//                    zapicDoctor.setMiddlename(dataSnapshot.getValue(ZapicDoctor.class).getMiddlename());
//                    zapicDoctor.setName(dataSnapshot.getValue(ZapicDoctor.class).getName());
//                    zapicDoctor.setNomerOMS(dataSnapshot.getValue(ZapicDoctor.class).getNomerOMS());
//                    zapicDoctor.setPhone(dataSnapshot.getValue(ZapicDoctor.class).getPhone());
//                    zapicDoctor.setSeriaOMS(dataSnapshot.getValue(ZapicDoctor.class).getSeriaOMS());
//                    zapicDoctor.setSnils(dataSnapshot.getValue(ZapicDoctor.class).getSnils());
//                    zapicDoctor.setCoutnChangeTime(dataSnapshot.getValue(ZapicDoctor.class).getCoutnChangeTime());
//                    zapicDoctor.setKey(dataSnapshot.getValue(ZapicDoctor.class).getKey());
//                    System.out.println(date);
//                    System.out.println(zapicDoctor.getEmail());
//
//                    arrayList2.add(zapicDoctor);
//                    System.out.println(arrayList2 + "ARRAYLIST2222");
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    System.out.println();
//                    historyAdapter.notifyDataSetChanged();
//                    System.out.println(FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//        flag = true;
    }
}