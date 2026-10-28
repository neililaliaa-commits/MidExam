package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE = "students";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "student_name";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_ENROLLED = "has_enrolled";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_STUDENT_ID + " TEXT NOT NULL UNIQUE, "
                + COL_ENROLLED + " INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public boolean addStudent(String name, String studentId, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_ENROLLED, enrolled ? 1 : 0);
        long result = db.insert(TABLE, null, values);
        return result != -1;
    }

    public boolean updateStudent(int id, String name, String studentId, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_STUDENT_ID, studentId);
        values.put(COL_ENROLLED, enrolled ? 1 : 0);
        int rows = db.update(TABLE, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.delete(TABLE, COL_ID + " = ?", new String[]{String.valueOf(id)});
        return rows > 0;
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE, null, null, null, null, null, COL_ID + " ASC");
        while (cursor.moveToNext()) {
            list.add(new Student(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_ENROLLED)) == 1
            ));
        }
        cursor.close();
        return list;
    }
}