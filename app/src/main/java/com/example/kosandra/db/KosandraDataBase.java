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
import com.example.kosandra.daos.HairstyleVisitDAO;
import com.example.kosandra.entity.Client;
import com.example.kosandra.entity.HairstyleVisit;
import com.example.kosandra.ui.client.Converters;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Executors;

@Database(entities = {Client.class, HairstyleVisit.class}, version = 1)
@TypeConverters(Converters.class)
public abstract class KosandraDataBase extends RoomDatabase {
    private static KosandraDataBase instance;

    public abstract ClientDAO clientDAO();

    public abstract HairstyleVisitDAO haircutDAO();
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
            HairstyleVisitDAO hairstyleVisitDAO = instance.haircutDAO();
            Executors.newSingleThreadExecutor().execute(() -> {
                String url = "https://i.pinimg.com/736x/1d/c4/14/1dc41457cab1335ea6147372add4686c--fractal-images-cat-paws.jpg";
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                try {
                    Bitmap bitmap = Picasso.get().load(url).get();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    Client client1 = new Client(byteArray, "Тигр", LocalDate.now(), "+79029442714", 2, 30, "Черный", "Средняя", "ss");
                    Client client2 = new Client(byteArray, "Тигр", LocalDate.now(), "+79029442714", 2, 30, "Черный", "Средняя","ss");

                    clientDAO.insert(client1);
                    clientDAO.insert(client2);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    };

}
