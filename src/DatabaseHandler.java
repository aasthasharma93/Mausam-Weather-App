package sharma.sugandha.mausam;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "cse321citydata";
 
    // Contacts table name
    private static final String TABLE_CITY = "citydata";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "city";
    private static final String KEY_ZIP = "zip";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CITY + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ZIP + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CITY);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    void addData(CityData cd) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        values.put(KEY_NAME, cd.getCity()); // Contact Name
        values.put(KEY_ZIP, cd.getZipCode()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_CITY, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single contact
    String getData(String city) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_CITY, new String[] { KEY_ID,
                KEY_NAME, KEY_ZIP }, KEY_NAME + "=?",
                new String[] { city }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        //CityData cd = new CityData(Integer.parseInt(cursor.getString(0)),cursor.getString(1), cursor.getString(2));
        // return contact
        return cursor.getString(2);
    }
 
    // Getting All Contacts
    public List<CityData> getAllData() {
        List<CityData> cdList = new ArrayList<CityData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CITY;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	CityData cd = new CityData();
                cd.set_id(Integer.parseInt(cursor.getString(0)));
                cd.setCity(cursor.getString(1));
                cd.setZipCode(cursor.getString(2));
                // Adding contact to list
                cdList.add(cd);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return cdList;
    }
}
