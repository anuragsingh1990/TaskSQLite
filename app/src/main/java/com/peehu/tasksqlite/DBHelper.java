package com.peehu.tasksqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.peehu.tasksqlite.Model.Task_Model;
import com.peehu.tasksqlite.Model.User_Model;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "task.db";
    public static final String TABLE_NAME = "user_table";
    public static final String ID = "ID";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";
    public static final String PASSWORD = "PASSWORD";

    public static final String USER_TABLE = "_table";
    public static final String IDs = "IDs";
    public static final String Title = "Title";
    public static final String Description = "Description";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL(" create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,NAME TEXT,EMAIL TEXT,PASSWORD TEXT)");
        MyDB.execSQL(" create table " + USER_TABLE + "(IDs INTEGER PRIMARY KEY AUTOINCREMENT,Title TEXT,Description TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {

        MyDB.execSQL("drop Table if exists " + TABLE_NAME);
        MyDB.execSQL("drop Table if exists " + USER_TABLE);
        onCreate(MyDB);

    }

    public boolean insertData(String name, String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        contentValues.put(PASSWORD, password);
        long result = MyDB.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.e("error", String.valueOf(result));
            return false;
        } else {
            return true;
        }
    }


    public boolean insertDataUser(String title, String description) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(Title, title);
        contentValues1.put(Description, description);

        long result = MyDB.insert(USER_TABLE, null, contentValues1);
        if (result == -1) {
            Log.e("error", String.valueOf(result));
            return false;
        } else {
            return true;
        }
    }


    public boolean checkUsername(String email) {

        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from " + TABLE_NAME + " where " + EMAIL + " like ?", new String[]{"%" + email + "%"});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkUsernamePassword(String email, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery(" select * from " + TABLE_NAME + " where " + EMAIL + "=? and " + PASSWORD + "=?", new String[]{email, password});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public ArrayList<User_Model> getAllUserList() {
        ArrayList<User_Model> user = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + TABLE_NAME, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //....   sthf = cursor.getString(cursor.getColumnIndex(SHUTOFFDATE));
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String email = cursor.getString(cursor.getColumnIndex(EMAIL));
                String id = cursor.getString(cursor.getColumnIndex(ID));


                User_Model userModel = new User_Model(id, name, email);


                user.add(userModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return user;
    }

    public ArrayList<Task_Model> getUsersData() {
        ArrayList<Task_Model> tasks = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + USER_TABLE, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                //....   sthf = cursor.getString(cursor.getColumnIndex(SHUTOFFDATE));
                String title = cursor.getString(cursor.getColumnIndex(Title));
                String dec = cursor.getString(cursor.getColumnIndex(Description));
                String id = cursor.getString(cursor.getColumnIndex(IDs));


                Task_Model taskModel = new Task_Model(id, title, dec);


                tasks.add(taskModel);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return tasks;
    }

    public Task_Model getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + USER_TABLE + " where IDs=" + id + "", null);


        if (cursor != null)
            cursor.moveToFirst();
        Task_Model task = new Task_Model(cursor.getString(0),
                cursor.getString(1), cursor.getString(2));

        return task;
    }

    public boolean update(int id, String title, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Title, title);
        contentValues.put(Description, description);

        db.update(USER_TABLE, contentValues, IDs + " = ? ",
                new String[]{String.valueOf(id)});

        return true;
    }

    public boolean updateUser(int id, String name, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);

        db.update(TABLE_NAME, contentValues, ID + " = ? ",
                new String[]{String.valueOf(id)});

        return true;
    }


    public void deleteStudents(Task_Model task_model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USER_TABLE, IDs + " = ? ",
                new String[]{String.valueOf(task_model.getId())});
        db.close();
    }

    public void deleteUser(User_Model user_model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ? ",
                new String[]{String.valueOf(user_model.getId())});
        db.close();
    }


}