package com.example.jens.gps_app;

/**
 * Created by Jens on 04.04.2016.
 */

import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqlManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaskDB";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_RANGE = "range";
    private static final String KEY_DESC = "desc";
    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE, KEY_LAT, KEY_LNG, KEY_RANGE, KEY_DESC};

    public SqlManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE tasks ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "lat DOUBLE, " +
                "lng DOUBLE, " +
                "range INTEGER, " +
                "desc TEXT )";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tasks");
        this.onCreate(db);
    }


    public void addTask(Task task) {
        Log.d("addTask", task.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_LAT, task.getLat());
        values.put(KEY_LNG, task.getLng());
        values.put(KEY_RANGE, task.getRange());
        values.put(KEY_DESC, task.getDesc());


        // 3. insert
        db.insert(TABLE_TASKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public Task getTASK(int id) {

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_TASKS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[]{String.valueOf(id)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build task object
        Task task = new Task();
        task.setId(Integer.parseInt(cursor.getString(0)));
        task.setTitle(cursor.getString(1));

        System.out.println("Task 2 auslesen: " + (cursor.getString(2)));
        task.setLat(Double.parseDouble(cursor.getString(2)));

        System.out.println("Task 3 auslesen: " + (cursor.getString(3)));
        task.setLng(Double.parseDouble(cursor.getString(3)));

        task.setRange(Integer.parseInt(cursor.getString(4)));
        task.setDesc(cursor.getString(5));

        Log.d("getTask(" + id + ")", task.toString());

        // 5. return task
        return task;
    }

    // Get All Books
    public List<Task> getAllTasks() {
        List<Task> tasks = new LinkedList<Task>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_TASKS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build task and add it to list
        Task task = null;
        if (cursor.moveToFirst()) {
            do {
                task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setTitle(cursor.getString(1));
                task.setLat(Double.parseDouble(cursor.getString(2)));
                task.setLng(Double.parseDouble(cursor.getString(3)));
                task.setRange(Integer.parseInt(cursor.getString(4)));
                task.setDesc(cursor.getString(5));

                // Add task to tasks
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        Log.d("getAllTasks()", tasks.toString());

        // return tasks
        return tasks;
    }

    // Updating single task
    public int updateBook(Task task) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", task.getTitle());
        values.put("lat", task.getLat());
        values.put("lng", task.getLng());
        values.put("range", task.getRange());
        values.put("desc", task.getDesc());

        // 3. updating row
        int i = db.update(TABLE_TASKS, //table
                values, // column/value
                KEY_ID + " = ?", // selections
                new String[]{String.valueOf(task.getId())}); //selection args

        // 4. close
        db.close();

        return i;

    }

    // Deleting single task
    public void deleteBook(Task task) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_TASKS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});

        // 3. close
        db.close();

        Log.d("deleteTask", task.toString());

    }
}