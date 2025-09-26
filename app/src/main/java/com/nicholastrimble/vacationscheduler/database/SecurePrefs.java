package com.nicholastrimble.vacationscheduler.database;

import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

public class SecurePrefs {
    public static void saveAlertTime(Context context, String key, long time) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            EncryptedSharedPreferences.create(
                    context,
                    "secure_vacation_alerts",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            ).edit().putLong(key, time).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}