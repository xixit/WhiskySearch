package geese.dk.whiskysearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import geese.dk.whiskysearch.helpers.Whisky;
import geese.dk.whiskysearch.helpers.WhiskySearch;

public class DatabaseHelper extends SQLiteOpenHelper
{
    // database name
    private static final String DATABASE_NAME = "whiskyDB.db";
    // database version
    private static final int DATABASE_VERSION = 1;
    // database table name
    public static final String TABLE_WHISKIES = "whiskies";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_STRENGTH = "strength";
    public static final String COLUMN_BOTTLED = "bottled";
    public static final String COLUMN_BOTTLER = "bottler";
    public static final String COLUMN_CASK_NUMBER = "caskNumber";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_IMAGE_URL = "imageURL";
    public static final String COLUMN_DETAILS_URL = "detailsURL";

    // database creation sql statement
    private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            TABLE_WHISKIES + "(" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_AGE + " TEXT, " +
            COLUMN_STRENGTH + " TEXT, " +
            COLUMN_BOTTLED + " TEXT, " +
            COLUMN_BOTTLER + " TEXT, " +
            COLUMN_CASK_NUMBER + " TEXT, " +
            COLUMN_RATING + " TEXT, " +
            COLUMN_IMAGE_URL + " TEXT, " +
            COLUMN_DETAILS_URL + " TEXT);";

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Log.v("DatabaseHelper", "Creating database!!!");
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)
    {
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS whiskies");
//        onCreate(sqLiteDatabase);
    }

    private static DatabaseHelper cDatabaseHelper;

    public static DatabaseHelper instance(Context context)
    {
        if(cDatabaseHelper == null)
            cDatabaseHelper = new DatabaseHelper(context);

        return cDatabaseHelper;
    }

    public void addWhisky(Whisky whisky)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, whisky.getName());
        values.put(COLUMN_AGE, whisky.getAge());
        values.put(COLUMN_STRENGTH, whisky.getStrength());
        values.put(COLUMN_BOTTLED, whisky.getBottled());
        values.put(COLUMN_BOTTLER, whisky.getBottler());
        values.put(COLUMN_CASK_NUMBER, whisky.getCaskNumber());
        values.put(COLUMN_RATING, whisky.getRating());
        values.put(COLUMN_IMAGE_URL, whisky.getImageURL());
        values.put(COLUMN_DETAILS_URL, whisky.getDetailsURL());

        // insert (TODO: or replace)
        db.insert(TABLE_WHISKIES, null, values);
        db.close();
    }

    public ArrayList<Whisky> getWhiskies(WhiskySearch search)
    {
        // list containing all found whiskies
        ArrayList<Whisky> whiskies = new ArrayList<Whisky>();

        // create query
        String query = "SELECT * FROM " + TABLE_WHISKIES;

        // speshul case ... WhiskyBase doesn't discern between distillery nor random name
        // sooo, just search for any whisky with the distillery in the name
        //if(!search.getDistillery().isEmpty())
            query += " WHERE " + COLUMN_NAME + " LIKE '%" + search.getDistillery() + "%'";

        // speshul case ... WhiskyBase doesn't discern between distillery nor random name
        // sooo, just search for any whisky with the name in the name
        //if(!search.getName().isEmpty())
            query += " OR " + COLUMN_NAME + " LIKE '%" + search.getName() + "%'";

        //if(!search.getAge().isEmpty())
            query += " OR " + COLUMN_AGE + " LIKE '%" + search.getAge() + "%'";

        //if(!search.getBottled().isEmpty())
            query += " OR " + COLUMN_BOTTLED + " LIKE '%" + search.getBottled() + "%'";

        //if(!search.getBottler().isEmpty())
            query += " OR " + COLUMN_BOTTLER + " LIKE '%" + search.getBottler() + "%'";

        // get database
        SQLiteDatabase db = this.getReadableDatabase();

        // run query
        Cursor res =  db.rawQuery(query, null );

        // iterate through results and get whiskies (if any)
        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            Whisky whisky = new Whisky();

            whisky.setName(res.getString(res.getColumnIndex(COLUMN_NAME)));
            whisky.setAge(res.getString(res.getColumnIndex(COLUMN_AGE)));
            whisky.setStrength(res.getString(res.getColumnIndex(COLUMN_STRENGTH)));
            whisky.setBottled(res.getString(res.getColumnIndex(COLUMN_BOTTLED)));
            whisky.setBottler(res.getString(res.getColumnIndex(COLUMN_BOTTLER)));
            whisky.setCaskNumber(res.getString(res.getColumnIndex(COLUMN_CASK_NUMBER)));
            whisky.setRating(res.getString(res.getColumnIndex(COLUMN_RATING)));
            whisky.setImageURL(res.getString(res.getColumnIndex(COLUMN_IMAGE_URL)));
            whisky.setDetailsURL(res.getString(res.getColumnIndex(COLUMN_DETAILS_URL)));

            whiskies.add(whisky);

            res.moveToNext();
        }

        // close database
        db.close();

        // finally return list of found whiskies
        return whiskies;
    }
}
