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
    private Timer timer = new Timer();
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
                }else {
                    date = ("0" + day + "0" + String.valueOf(month) + year);
                }
                date = removePunct2(date);

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


        ArrayList<String> arrayList = new ArrayList<>();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mDataBase.child("User").child(currentUser.getUid());
        System.out.println(currentUser.getUid());
        mDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (arrayList.size() == 0) {
                            User user = new User();
                            user.setBirhday(dataSnapshot.getValue(User.class).getBirhday());
                            data.setText(user.getBirhday());
                            arrayList.add("dasd");
                            user.setEmail(dataSnapshot.getValue(User.class).getEmail());
                            email.setText(user.getEmail());
                            user.setFirstname(dataSnapshot.getValue(User.class).getFirstname());
                            name.setText(user.getFirstname());
                            user.setLastname(dataSnapshot.getValue(User.class).getLastname());
                            surname.setText(user.getLastname());
                            user.setMiddlename(dataSnapshot.getValue(User.class).getMiddlename());
                            name_of_father.setText(user.getMiddlename());
                            user.setNomerOMS(dataSnapshot.getValue(User.class).getNomerOMS());
                            polis_number.setText(user.getNomerOMS());
                            user.setPhone(dataSnapshot.getValue(User.class).getPhone());
                            phone.setText(user.getPhone());
                            user.setSeriaOMS(dataSnapshot.getValue(User.class).getSeriaOMS());
                            polis.setText(user.getSeriaOMS());
                            user.setSnils(dataSnapshot.getValue(User.class).getSnils());
                            snils.setText(user.getSnils());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    String lastname = surname.getText().toString();
                    String Name = name.getText().toString();
                    String middlename = name_of_father.getText().toString();
                    String birthday = data.getText().toString();
                    String Snils = snils.getText().toString();
                    String Email = email.getText().toString();
                    String Phone = phone.getText().toString();
                    String seriaOMS = polis.getText().toString();
                    String nomerOMS = polis_number.getText().toString();
                    String doctor = chosheDoctor;
                    int index = listView.getCheckedItemPosition();
                    Shedule shedule = shedule2.get(index);
                    String date = shedule.getDate();
                    String time = shedule.getTime();
                    date = removePunct2(date);

                    ZapicDoctor zapicDoctor = null;

                    if (doctor.equals("Отоларинголог")) {
                        zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                                Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "1", "0", shedule.time);
                    } else if (doctor.equals("Терапевт")) {
                        zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                                Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "2", "0", shedule.time);
                    } else if (doctor.equals("Хирург")) {
                        zapicDoctor = new ZapicDoctor(lastname, Name, middlename, birthday, Snils,
                                Email, Phone, seriaOMS, nomerOMS, doctor, shedule.date, shedule.time, "3", "0", shedule.time);
                    }

                    if (doctor.equals("Отоларинголог")) {
                        myRef3.child("Запись отоларинголог").child(date).child(time).setValue(zapicDoctor);
//                    myRef4.child("Отоларинголог").child(date).child(time).removeValue();
                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                        String finalDate = date;
                        myRef4.child("Отоларинголог").child(date);
                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        String key = datas.getKey();
                                        myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
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
                        intent.putExtra("date", zapicDoctor.getData());
                        intent.putExtra("time", zapicDoctor.getTime());
                        intent.putExtra("doctor", zapicDoctor.getDoctor());
                        intent.putExtra("snils", zapicDoctor.getSnils());
                        getActivity().startService(intent);
                    } else if (doctor.equals("Терапевт")) {
                        myRef3.child("Запись терапевт").child(date).child(time).setValue(zapicDoctor);
                        myRef4 = mFirebaseDatabase.getReference("User").child(zapicDoctor.getDoctor()).child(date);
                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                        String finalDate = date;
                        System.out.println(finalDate);
                        System.out.println(finalZapicDoctor.getTime());
                        System.out.println(zapicDoctor.getDoctor());
                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        String key = datas.getKey();
                                        System.out.println(key);
                                        myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
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
                        intent.putExtra("date", zapicDoctor.getData());
                        intent.putExtra("time", zapicDoctor.getTime());
                        intent.putExtra("doctor", zapicDoctor.getDoctor());
                        intent.putExtra("snils", zapicDoctor.getSnils());
                        getActivity().startService(intent);
                    } else if (doctor.equals("Хирург")) {
                        myRef3.child("Запись хирург").child(date).child(time).setValue(zapicDoctor);
                        ZapicDoctor finalZapicDoctor = zapicDoctor;
                        String finalDate = date;
                        myRef4.child("Хирург").child(date);
                        myRef4.orderByChild("time").equalTo(zapicDoctor.getTime()).limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        String key = datas.getKey();
                                        myRef4 = mFirebaseDatabase.getReference("User").child(finalZapicDoctor.getDoctor()).child(finalDate)
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
                        intent.putExtra("date", zapicDoctor.getData());
                        intent.putExtra("time", zapicDoctor.getTime());
                        intent.putExtra("doctor", zapicDoctor.getDoctor());
                        intent.putExtra("snils", zapicDoctor.getSnils());
                    }
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

    private void showData(DataSnapshot snapshot) {
        for (DataSnapshot ds : snapshot.getChildren()){
            User uInfo = new User();
            uInfo.setFirstname(ds.child(userID).getValue(User.class).getFirstname());
            uInfo.setLastname(ds.child(userID).getValue(User.class).getLastname());
            uInfo.setMiddlename(ds.child(userID).getValue(User.class).getMiddlename());
            uInfo.setBirhday(ds.child(userID).getValue(User.class).getBirhday());
            uInfo.setSnils(ds.child(userID).getValue(User.class).getSnils());
            uInfo.setEmail(ds.child(userID).getValue(User.class).getEmail());
            uInfo.setPhone(ds.child(userID).getValue(User.class).getPhone());
            uInfo.setSeriaOMS(ds.child(userID).getValue(User.class).getSeriaOMS());
            uInfo.setNomerOMS(ds.child(userID).getValue(User.class).getNomerOMS());

            surname.setText(uInfo.getLastname());
            name.setText(uInfo.getFirstname());
            name_of_father.setText(uInfo.getMiddlename());
            data.setText(uInfo.getBirhday());
            snils.setText(uInfo.getSnils());
            email.setText(uInfo.getEmail());
            phone.setText(uInfo.getPhone());
            polis.setText(uInfo.getSeriaOMS());
            polis_number.setText(uInfo.getNomerOMS());
        }
    }
}
