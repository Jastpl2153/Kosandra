package com.example.kosandra.db;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kosandra.daos.ClientDAO;
import com.example.kosandra.daos.ExpensesDAO;
import com.example.kosandra.daos.HairstyleVisitDAO;
import com.example.kosandra.daos.IncomeDAO;
import com.example.kosandra.daos.MaterialsDAO;
import com.example.kosandra.daos.RecordsDAO;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.Expenses;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.entity.Income;
import com.example.kosandra.entity.Materials;
import com.example.kosandra.entity.Record;
import com.example.kosandra.ui.general_logic.Converters;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.Executors;

@Database(entities = {Client.class, HairstyleVisit.class, Materials.class, Expenses.class, Income.class, Record.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class KosandraDataBase extends RoomDatabase {
    private static KosandraDataBase instance;

    public abstract ClientDAO clientDAO();

    public abstract HairstyleVisitDAO haircutDAO();

    public abstract MaterialsDAO materialsDAO();

    public abstract ExpensesDAO expensesDAO();

    public abstract IncomeDAO incomeDAO();
    public abstract RecordsDAO recordsDAO();

    public static synchronized KosandraDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), KosandraDataBase.class, "kosandra_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            ClientDAO clientDAO = instance.clientDAO();
            MaterialsDAO materialsDAO = instance.materialsDAO();
            HairstyleVisitDAO hairstyleVisitDAO = instance.haircutDAO();
            ExpensesDAO expensesDAO = instance.expensesDAO();
            IncomeDAO incomeDAO = instance.incomeDAO();
            RecordsDAO recordsDAO = instance.recordsDAO();

            Executors.newSingleThreadExecutor().execute(() -> {
                String url = "https://i.pinimg.com/736x/e2/c2/9f/e2c29f03374efcf7994b3559683103b9.jpg";
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    Bitmap bitmap = Picasso.get().load(url).get();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Client client1 = new Client(byteArray, "Анджелина Джоли",
                            LocalDate.parse("1975-06-04"),
                            "+79029442714",
                            2,
                            30,
                            "Черный",
                            "Средняя",
                            "О сьемках фильма");

                    Client client2 = new Client(byteArray, "Мерлин Монро",
                            LocalDate.parse("1926-06-01"),
                            "+79029442714",
                            1,
                            30,
                            "Блонд",
                            "Средняя",
                            "О концертах и гастролях");

                    clientDAO.insert(client1);
                    clientDAO.insert(client2);

                    Materials materials = new Materials("Канекалон",
                            "Синий", "1", byteArray,
                            5, 600, "Взрослый",
                            "Аида", null, null, 2);

                    Materials materials1 = new Materials("Канекалон",
                            "Красный", "2", byteArray,
                            5, 700, "Детский",
                            "Вау Джау", null, null, 0);

                    Materials materials2 = new Materials("Кудри",
                            "Коричневый", "3", byteArray,
                            5, 1000, null, null,
                            "Ариэль", 25, 1);

                    Materials materials3 = new Materials("Кудри",
                            "Блонд", "4", byteArray,
                            5, 1200, null, null,
                            "Моника", 80, 1);

                    Materials materials4 = new Materials("Термоволокно",
                            "Желтый", "5", byteArray,
                            5, 900, null, null,
                            null, null, 0);

                    Materials materials5 = new Materials("Термоволокно",
                            "Зеленый", "6", byteArray,
                            5, 555, null, null,
                            null, null, 0);

                    materialsDAO.insert(materials);
                    materialsDAO.insert(materials1);
                    materialsDAO.insert(materials2);
                    materialsDAO.insert(materials3);
                    materialsDAO.insert(materials4);
                    materialsDAO.insert(materials5);

                    HairstyleVisit visit = new HairstyleVisit(LocalDate.parse("2024-03-27"),
                            byteArray, "5 брейдов", 1,
                            2000, 600, LocalTime.parse("02:10:00"),
                            300, new String[]{"1"}, new int[]{1});

                    HairstyleVisit visit2 = new HairstyleVisit(LocalDate.parse("2024-03-26"),
                            byteArray, "Водопад", 1,
                            1800, 600, LocalTime.parse("01:00:00"),
                            500, new String[]{"3"}, new int[]{1});

                    HairstyleVisit visit3 = new HairstyleVisit(LocalDate.parse("2024-03-25"),
                            byteArray, "Кудри PUSH-UP", 2,
                            7000, 2000, LocalTime.parse("04:05:00"),
                            800, new String[]{"4"}, new int[]{1});

                    hairstyleVisitDAO.insert(visit);
                    hairstyleVisitDAO.insert(visit2);
                    hairstyleVisitDAO.insert(visit3);

                    Expenses expenses1 = new Expenses("Инструмент", "Ножницы", 500, LocalDate.parse("2024-03-20"));
                    Expenses expenses2 = new Expenses("Рабочее место", "Стул", 20000, LocalDate.parse("2024-03-22"));
                    Expenses expenses3 = new Expenses("Украшения", "Кольца", 120, LocalDate.parse("2024-03-21"));
                    Expenses expenses4 = new Expenses("Обучение", "Курс ", 200, LocalDate.parse("2024-03-23"));

                    expensesDAO.insert(expenses1);
                    expensesDAO.insert(expenses2);
                    expensesDAO.insert(expenses3);
                    expensesDAO.insert(expenses4);

                    Income income1 = new Income("Уход за прической", "Пенка", 500, LocalDate.parse("2024-03-26"));
                    Income income2 = new Income("Другое", "Шапочка", 200, LocalDate.parse("2024-03-25"));
                    Income income3 = new Income("Украшение", "Кольца", 60, LocalDate.parse("2024-03-27"));

                    incomeDAO.insert(income1);
                    incomeDAO.insert(income2);
                    incomeDAO.insert(income3);

                    Record record1 = new Record(1, LocalDate.parse("2024-04-26"), LocalTime.parse("10:00:00"), "Водопад", 2000);
                    Record record2 = new Record(2, LocalDate.parse("2024-04-26"), LocalTime.parse("14:00:00"), "Водопад", 2000);
                    Record record3 = new Record(1, LocalDate.parse("2024-04-10"), LocalTime.parse("09:10:00"), "Cabyrc", 2500);

                    recordsDAO.insert(record1);
                    recordsDAO.insert(record2);
                    recordsDAO.insert(record3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    };
}
