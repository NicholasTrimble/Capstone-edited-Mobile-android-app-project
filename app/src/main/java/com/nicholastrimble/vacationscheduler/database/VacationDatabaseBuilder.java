package com.nicholastrimble.vacationscheduler.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


import com.nicholastrimble.vacationscheduler.dao.ExcursionDAO;
import com.nicholastrimble.vacationscheduler.dao.VacationDAO;
import com.nicholastrimble.vacationscheduler.entities.Excursion;
import com.nicholastrimble.vacationscheduler.entities.Vacation;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SupportFactory;

import java.nio.charset.StandardCharsets;

@Database(entities = {Vacation.class, Excursion.class}, version = 1)
public abstract class VacationDatabaseBuilder extends RoomDatabase {

    public abstract VacationDAO vacationDAO();
    public abstract ExcursionDAO excursionDAO();

    private static volatile VacationDatabaseBuilder INSTANCE;
    private static final String DB_PASSWORD = "YourSecurePassword123"; // Change this!

    static VacationDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VacationDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    // Initialize SQLCipher FIRST
                    SQLiteDatabase.loadLibs(context);

                    // Simple password for development
                    byte[] passphrase = "test123".getBytes(StandardCharsets.UTF_8);
                    SupportFactory factory = new SupportFactory(passphrase);

                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    VacationDatabaseBuilder.class,
                                    "MyVacationDatabase.db")
                            .openHelperFactory(factory)
                            .fallbackToDestructiveMigration() // Destroys old unencrypted DB
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}