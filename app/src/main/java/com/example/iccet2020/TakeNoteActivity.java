package com.example.iccet2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TakeNoteActivity extends AppCompatActivity {

    private EditText surname, name, name_of_father, data, snils, email, password, phone, polis, polis_number;
    private Button registration_btn;

    private ArrayList countries;
    private ArrayAdapter adapterForSpinner;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_note);

        init();//вызов метода

        countries = new ArrayList<>();
        countries.add("Педиатр");
        countries.add("Психолог");
        countries.add("Лор");
        countries.add("Акушер-гинеколог");
        countries.add("Аллерголог-иммунолог");
        countries.add("Аллерголог");
        countries.add("Ангиохирург");




        adapterForSpinner = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, countries);
        adapterForSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterForSpinner);

        registration_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.registration_btn){
                    Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                    startActivity(intent);
                }
            }
        });


        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year,
                                            int month, int dayOfMonth) {
                int mYear = year;
                int mMonth = month;
                int mDay = dayOfMonth;
                String selectedDate = new StringBuilder().append(mMonth + 1)
                        .append("-").append(mDay).append("-").append(mYear)
                        .append(" ").toString();
                Toast.makeText(getApplicationContext(), selectedDate, Toast.LENGTH_LONG).show();
            }
        });

    }


    //иницилизация

    private void init(){
        spinner = findViewById(R.id.spinner);

        surname = findViewById(R.id.surname);
        name = findViewById(R.id.name);
        name_of_father = findViewById(R.id.name_of_father);
        data = findViewById(R.id.data);
        snils = findViewById(R.id.snils);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        phone = findViewById(R.id.phone);
        polis = findViewById(R.id.polis);
        polis_number = findViewById(R.id.polis_number);

        registration_btn = findViewById(R.id.registration_btn);
    }
}


/*
*
*

Акушер-гинеколог
Аллерголог-иммунолог
Аллерголог
Ангиохирург
Андролог-эндокринолог
Андролог
Анестезиолог-реаниматолог
Анестезиолог
Аритмолог
Ароматерапевт
Артролог
Бактериолог
Бальнеолог
Валеолог
Венеролог
Вертебролог
Вирусолог
Врач УЗИ
Врач общей практики
Врач по лечебной физкультуре и спорту
Врач по лечению бесплодия
Врач по спортивной медицине
Врач скорой помощи
Врач функциональной диагностики
Гастроэнтеролог
Гематолог
Генетик
Гепатолог
Гериатр
Гинеколог-перинатолог
Гинеколог-эндокринолог
Гинеколог
Гирудотерапевт
Гомеопат
Дерматовенеролог
Дерматолог
Диагност
Диетолог
Зубной врач
Иглорефлексотерапевт
Иммунолог
Имплантолог
Инфекционист
Кардиолог
Кардиоревматолог
Кардиохирург
Кинезиолог
Колопроктолог
Косметолог
Курортолог
ЛОР
Лаборант
Логопед
Маммолог
Мануальный терапевт
Массажист
Миколог
Нарколог
Невролог
Невропатолог
Нейротравматолог
Нейрохирург
Нефролог
Окулист
Онкогинеколог
Онколог-гинеколог
Онколог-хирург
Онколог
Онкоуролог
Ортодонт
Ортопед-травматолог
Ортопед
Остеопат
Отоларинголог
Отоневролог
Офтальмолог-хирург
Офтальмолог
Паразитолог
Паркинсонолог
Пародонтолог
Пластический хирург
Подолог
Проктолог
Профпатолог
Психиатр-нарколог
Психиатр
Психолог
Психоневролог
Психотерапевт
Пульмонолог
Радиолог
Реабилитолог
Реаниматолог
Ревматолог
Рентгенолог
Репродуктолог
Рефлексотерапевт
Санитарный врач по гигиене питания
Санитарный врач по гигиене труда
Сексолог
Сексопатолог
Семейный врач
Семейный доктор
Сомнолог
Сосудистый хирург
Специалист восстановительного лечения
Стоматолог-ортодонт
Стоматолог-ортопед
Стоматолог-протезист
Стоматолог-терапевт
Стоматолог-хирург
Стоматолог
Суггестолог
Судебно-медицинский эксперт
Сурдолог
Терапевт мануальный
Терапевт
Токсиколог
Торакальный хирург
Травматолог
Трансфузиолог
Трихолог
УЗИ врач
Урогинеколог
Уролог
Фармаколог клинический
Физиотерапевт
Флеболог
Фониатр
Фтизиатр
Хирург пластический
Хирург сосудистый
Хирург торакальный
Хирург челюстно-лицевой
Хирург-флеболог
Хирург
Эмбриолог
Эндодонт
Эндокринолог
Эндоскопист
Эпидемиолог
Эпилептолог
*
*
* */