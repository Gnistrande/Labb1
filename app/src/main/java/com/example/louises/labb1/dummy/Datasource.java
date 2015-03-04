package com.example.louises.labb1.dummy;

/**
 * Created by Louise on 04/03/15.
 */

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;

        import java.util.ArrayList;

public class Datasource extends SQLiteOpenHelper {

    // Database fields
    private SQLiteDatabase database;

    public static final String TABLE = "items";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_DESCRIPTION = "description";

    public static String[] allColumns = { COLUMN_ID,
            COLUMN_TITLE, COLUMN_RATING, COLUMN_DESCRIPTION };

    private static final String DATABASE_NAME = "items.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "+ TABLE
            + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_RATING + " integer, "
            + COLUMN_DESCRIPTION + " text);";

    public Datasource(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(Datasource.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }


    public long insertItem(String title, int rating, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_DESCRIPTION, description);
        long rowId = database.insert(TABLE, null, values);
        return rowId;
    }

    public void updateItem(long id, String title, int rating, String description) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_RATING, rating);
        values.put(COLUMN_DESCRIPTION, description);
        database.update(TABLE,values, COLUMN_ID+ " = " + id, null);
    }

    public void deleteItem(String id) {
        database.delete(TABLE, COLUMN_ID
                + " = " + id, null);
    }

    public ArrayList<Item> fetchAll(int sortingColumn, boolean ascending){
        ArrayList<Item> items = new ArrayList<>();

        String column = allColumns[sortingColumn];
        String order = ascending ? " COLLATE NOCASE ASC":" COLLATE NOCASE DESC";
        String sortOrder = column + order;

        Log.i(Datasource.class.getName(), "Making query, order by " + sortOrder);
        Cursor cursor = database.query(TABLE,
                allColumns, null, null, null, null, sortOrder);

        Log.i(Datasource.class.getName(), "Processing result set, found "+cursor.getCount()+" entries");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Item item = new Item();
            item.setId(cursor.getLong(0));
            item.setTitle(cursor.getString(1));
            item.setRating(cursor.getInt(2));
            item.setDescription(cursor.getString(3));
            items.add(item);
            cursor.moveToNext();
        }
        // Make sure to close the cursor
        cursor.close();
        Log.i(Datasource.class.getName(), "Processing finished, returning data set");
        return items;
    }
}