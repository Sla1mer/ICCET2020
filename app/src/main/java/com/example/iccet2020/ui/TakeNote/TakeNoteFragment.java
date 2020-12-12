package com.example.iccet2020.ui.TakeNote;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.iccet2020.R;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.iccet2020.Shedule;
import com.example.iccet2020.Shifr;
import com.example.iccet2020.User;
import com.example.iccet2020.ZapicDoctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;


public class TakeNoteFragment extends Fragment {

    private TakeNoteViewModel dashboardViewModel;
    private EditText surname, name, name_of_father, data, snils, email, phone, polis, polis_number;
    private Button registration_btn, openCalendar;
    private ListView listView;
    private ArrayList<Shedule> shedule2;
    private ArrayList<String> shedule3;
    private ArrayAdapter<String> adapter;
    private String chosheDoctor;

    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    private String userID;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    private DatabaseReference myRef3;
    private DatabaseReference myRef4;
    private DatabaseReference myRef2;
    private DatabaseReference myRef5;
    private String USER_KEY = "User";
    private static final String PUNCT = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    private Shifr shifr = new Shifr();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String date = " ";
    private DatabaseReference mDataBase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(TakeNoteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_take_note, container, false);


        spinner = root.findViewById(R.id.spinner);

        surname = root.findViewById(R.id.surname);
        name = root.findViewById(R.id.name);
        name_of_father = root.findViewById(R.id.name_of_father);
        data = root.findViewById(R.id.data);
        snils = root.findViewById(R.id.snils);
        email = root.findViewById(R.id.email);
        phone = root.findViewById(R.id.phone);
        polis = root.findViewById(R.id.polis);
        polis_number = root.findViewById(R.id.polis_number);
        myRef3 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        myRef4 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        myRef5 = FirebaseDatabase.getInstance().getReference(USER_KEY);
        openCalendar = root.findViewById(R.id.openCalendar);

        registration_btn = root.findViewById(R.id.registration_btn);
        // получаем экземпляр элемента ListView
        listView = root.findViewById(R.id.listView);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        shedule2 = new ArrayList<Shedule>();
        shedule3 = new ArrayList<String>();

        // Создаём адаптер ArrayAdapter, чтобы привязать массив к ListView
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, shedule3);

        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = FirebaseAuth.getInstance().getUid();

        countries = new ArrayList<>();
        countries.add("Выберите врача");
        countries.add("Отоларинголог");
        countries.add("Терапевт");
        countries.add("Хирург");
        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);

        adapterForSpinner = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, countries) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                if (String.valueOf(day).length() == 1)
                {
                    date = ("0" + day + String.valueOf(month) + year);
                }else if (String.valueOf(month).length() == 1)
                {
                    date = (day + "0" + String.valueOf(month) + year);
                }else if (String.valueOf(month).length() == 1 && String.valueOf(day).length() == 1){
                    date = ("0" + day + "0" + String.valueOf(month) + year);
                }else {
                    date = (day + String.valueOf(month) + year);
                }
                date = removePunct2(date);

                System.out.println(date + " dasqwepqweqw");
                System.out.println(chosheDoctor + " dasqwepqweqw");

                switch (chosheDoctor) {
                    case "Хирург":
                        shedule2.clear();
                        shedule3.clear();
                        myRef2 = mFirebaseDatabase.getReference("User").child("Хирург").child(date);
                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                shedule2.clear();
                                shedule3.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Shedule shedule = new Shedule();
                                    shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                    shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                    shedule2.add(shedule);
                                    shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        break;
                    case "Терапевт":

                        mDataBase = FirebaseDatabase.getInstance().getReference(USER_KEY);
                        myRef = mDataBase.child("Терапевт").child(date);
                        mDataBase.child("Терпавет").child(date);
                        System.out.println(date);

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                shedule2.clear();
                                shedule3.clear();
                                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                        Shedule shedule = new Shedule();
                                        shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                        shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                        System.out.println(shedule.getDate() + " " + shedule.getTime());
                                        shedule2.add(shedule);
                                        shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                        System.out.println(shedule3);
                                        adapter.notifyDataSetChanged();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        adapter.notifyDataSetChanged();

                        break;
                    case "Отоларинголог":
                        shedule2.clear();
                        shedule3.clear();
                        myRef2 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(date);

                        myRef2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                shedule2.clear();
                                shedule3.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Shedule shedule = new Shedule();
                                    shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                    shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                    shedule2.add(shedule);
                                    shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        adapter.notifyDataSetChanged();
                        break;
                }
                System.out.println(shedule2);

            }
        };

        openCalendar.setOnClickListener(new View.OnClickListener() {
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

        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        getData();

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
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

                                    user.setBirhday(dataSnapshot.getValue(User.class).getBirhday());
                                    user.setSnils(dataSnapshot.getValue(User.class).getSnils());
                                    user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                                    user.setPhone(dataSnapshot.getValue(User.class).getPhone());
                                    user.setSeriaOMS(dataSnapshot.getValue(User.class).getSeriaOMS());
                                    user.setNomerOMS(dataSnapshot.getValue(User.class).getNomerOMS());

                                    String doctor = chosheDoctor;
                                    int index = listView.getCheckedItemPosition();
                                    Shedule shedule = shedule2.get(index);
                                    String date = shedule.getDate();
                                    String time = shedule.getTime();
                                    date = removePunct2(date);
                                    System.out.println(date + "DATE PLK");

                                    ZapicDoctor zapicDoctor = null;

                                    if (doctor.equals("Отоларинголог")) {
                                        zapicDoctor = new ZapicDoctor(user.getLastname(), user.getFirstname(), user.getMiddlename(), user.getBirhday(), user.getSnils(),
                                                user.getEmail(), user.getPhone(), user.getSeriaOMS(), user.getNomerOMS(), shifr.hifr_zezarya(doctor), shifr.hifr_zezarya(shedule.date), shedule.time, shifr.hifr_zezarya("1"), "0", shedule.time);
                                    } else if (doctor.equals("Терапевт")) {
                                        zapicDoctor = new ZapicDoctor(user.getLastname(), user.getFirstname(), user.getMiddlename(), user.getBirhday(), user.getSnils(),
                                                user.getEmail(), user.getPhone(), user.getSeriaOMS(), user.getNomerOMS(), shifr.hifr_zezarya(doctor), shifr.hifr_zezarya(shedule.date), shedule.time, shifr.hifr_zezarya("2"), "0", shedule.time);
                                    } else if (doctor.equals("Хирург")) {
                                        zapicDoctor = new ZapicDoctor(user.getLastname(), user.getFirstname(), user.getMiddlename(), user.getBirhday(), user.getSnils(),
                                                user.getEmail(), user.getPhone(), user.getSeriaOMS(), user.getNomerOMS(), shifr.hifr_zezarya(doctor), shifr.hifr_zezarya(shedule.date), shedule.time, shifr.hifr_zezarya("3"), "0", shedule.time);
                                    }

                                    if (doctor.equals("Отоларинголог")) {
                                        myRef3.child("Запись отоларинголог").child(date).child(time).setValue(zapicDoctor);
                                        System.out.println("DATE DATE DATE " + date);
                                        myRef4 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(date);
                                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                                        String finalDate = date;
                                        System.out.println(finalDate);
                                        System.out.println(finalZapicDoctor.getTime());
                                        System.out.println(zapicDoctor.getDoctor());


                                        System.out.println("TIME TIME TIME " + zapicDoctor.getTime());
                                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                                        String key = datas.getKey();
                                                        System.out.println(key);
                                                        myRef4 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(finalDate)
                                                                .child(key);
                                                        myRef4.removeValue();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Intent intent = new Intent(getContext(), com.example.iccet2020.Services.Service.class);
                                        intent.putExtra("date", date);
                                        intent.putExtra("timeO", zapicDoctor.getTime());
                                        intent.putExtra("doctorO", "отоларинголог");
                                        intent.putExtra("snils", zapicDoctor.getSnils());
                                        getActivity().startService(intent);
                                        getActivity().startService(intent);
                                    } else if (doctor.equals("Терапевт")) {
                                        myRef3.child("Запись терапевт").child(date).child(time).setValue(zapicDoctor);
                                        System.out.println("DATE DATE DATE " + date);
                                        myRef4 = mFirebaseDatabase.getReference("User").child("Терапевт").child(date);
                                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                                        String finalDate = date;
                                        System.out.println(finalDate);
                                        System.out.println(finalZapicDoctor.getTime());
                                        System.out.println(zapicDoctor.getDoctor());


                                        System.out.println("TIME TIME TIME " + zapicDoctor.getTime());
                                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                                        String key = datas.getKey();
                                                        System.out.println(key);
                                                        myRef4 = mFirebaseDatabase.getReference("User").child("Терапевт").child(finalDate)
                                                                .child(key);
                                                        myRef4.removeValue();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Intent intent = new Intent(getContext(), com.example.iccet2020.Services.Service.class);
                                        intent.putExtra("date", date);
                                        intent.putExtra("timeT", zapicDoctor.getTime());
                                        intent.putExtra("doctorT", "терапевт");
                                        intent.putExtra("snils", zapicDoctor.getSnils());
                                        getActivity().startService(intent);
                                    } else if (doctor.equals("Хирург")) {
                                        myRef3.child("Запись хирург").child(date).child(time).setValue(zapicDoctor);
                                        System.out.println("DATE DATE DATE " + date);
                                        myRef4 = mFirebaseDatabase.getReference("User").child("Хирург").child(date);
                                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                                        String finalDate = date;
                                        System.out.println(finalDate);
                                        System.out.println(finalZapicDoctor.getTime());
                                        System.out.println(zapicDoctor.getDoctor());


                                        System.out.println("TIME TIME TIME " + zapicDoctor.getTime());
                                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                                        String key = datas.getKey();
                                                        System.out.println(key);
                                                        myRef4 = mFirebaseDatabase.getReference("User").child("Хирург").child(finalDate)
                                                                .child(key);
                                                        myRef4.removeValue();
                                                        adapter.notifyDataSetChanged();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Intent intent = new Intent(getContext(), com.example.iccet2020.Services.Service.class);
                                        intent.putExtra("date", date);
                                        intent.putExtra("timeX", zapicDoctor.getTime());
                                        intent.putExtra("doctorX", "хирург");
                                        intent.putExtra("snils", zapicDoctor.getSnils());
                                        getActivity().startService(intent);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }catch (Exception ex){
                    System.out.println(ex.getMessage());
                }
            }
        });


        myRef2 = mFirebaseDatabase.getReference();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                chosheDoctor = ((TextView) view).getText().toString();
                System.out.println(chosheDoctor);

                if (chosheDoctor.equals("Хирург")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Хирург").child(date);
                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                } else if (chosheDoctor.equals("Терапевт")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Терапевт").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else if (chosheDoctor.equals("Отоларинголог")) {
                    shedule2.clear();
                    shedule3.clear();
                    myRef2 = mFirebaseDatabase.getReference("User").child("Отоларинголог").child(date);

                    myRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            shedule2.clear();
                            shedule3.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                Shedule shedule = new Shedule();
                                shedule.setDate(dataSnapshot.getValue(Shedule.class).getDate());
                                shedule.setTime(dataSnapshot.getValue(Shedule.class).getTime());
                                shedule2.add(shedule);
                                shedule3.add("Дата: " + shedule.getDate() + "\n" + "Время: " + shedule.getTime());
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    adapter.notifyDataSetChanged();
                }
                System.out.println(shedule2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        return root;

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

                        System.out.println(user.getBirhday());

                        name.setText(user.getFirstname());
                        surname.setText(user.getLastname());
                        name_of_father.setText(user.getMiddlename());
                        data.setText(user.getBirhday());
                        snils.setText(user.getSnils());
                        email.setText(user.getEmail());
                        phone.setText(user.getPhone());
                        polis.setText(user.getSeriaOMS());
                        polis_number.setText(user.getNomerOMS());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
