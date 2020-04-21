package com.example.discard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "barcodeStore.db";
    public static final int VERSION = 1;
    public static final String TABLE = "BarcodeTable";  // название таблицы в бд
    public static final String COLUMN_ID = "User_ID";
    public static final String COLUMN_BC = "Barcode";
    public static final String COLUMN_NM = "Name";
    public static final String COLUMN_TP = "Type";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_BC + " TEXT, "
                + COLUMN_NM + " TEXT, "
                + COLUMN_TP + " INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }


}
