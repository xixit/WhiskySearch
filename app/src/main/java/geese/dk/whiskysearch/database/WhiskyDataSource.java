package geese.dk.whiskysearch.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import geese.dk.whiskysearch.helpers.Whisky;
import geese.dk.whiskysearch.helpers.WhiskySearch;

public class WhiskyDataSource
{
    // Database fields
    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDbHelper;

    public WhiskyDataSource(Context context)
    {
        mDbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException
    {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close()
    {
        mDbHelper.close();
    }

    public void addWhisky(Whisky whisky)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, whisky.getName());
        values.put(DatabaseHelper.COLUMN_AGE, whisky.getAge());
        values.put(DatabaseHelper.COLUMN_STRENGTH, whisky.getStrength());
        values.put(DatabaseHelper.COLUMN_BOTTLED, whisky.getBottled());
        values.put(DatabaseHelper.COLUMN_BOTTLER, whisky.getBottler());
        values.put(DatabaseHelper.COLUMN_CASK_NUMBER, whisky.getCaskNumber());
        values.put(DatabaseHelper.COLUMN_RATING, whisky.getRating());
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, whisky.getImageURL());
        values.put(DatabaseHelper.COLUMN_DETAILS_URL, whisky.getDetailsURL());

        // insert or replace
        mDatabase.replace(DatabaseHelper.TABLE_WHISKIES, null, values);
        mDatabase.close();
    }

    public ArrayList<Whisky> getWhiskies(WhiskySearch search)
    {
        // list containing all found whiskies
        ArrayList<Whisky> whiskies = new ArrayList<Whisky>();

        // create query
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_WHISKIES;

        // speshul case ... WhiskyBase doesn't discern between distillery nor random name
        // sooo, just search for any whisky with the distillery in the name
        //if(!search.getDistillery().isEmpty())
        query += " WHERE " + DatabaseHelper.COLUMN_NAME + " LIKE '%" + search.getDistillery() + "%'";

        // speshul case ... WhiskyBase doesn't discern between distillery nor random name
        // sooo, just search for any whisky with the name in the name
        //if(!search.getName().isEmpty())
        query += " OR " + DatabaseHelper.COLUMN_NAME + " LIKE '%" + search.getName() + "%'";

        //if(!search.getAge().isEmpty())
        query += " OR " + DatabaseHelper.COLUMN_AGE + " LIKE '%" + search.getAge() + "%'";

        //if(!search.getBottled().isEmpty())
        query += " OR " + DatabaseHelper.COLUMN_BOTTLED + " LIKE '%" + search.getBottled() + "%'";

        //if(!search.getBottler().isEmpty())
        query += " OR " + DatabaseHelper.COLUMN_BOTTLER + " LIKE '%" + search.getBottler() + "%'";

        Cursor res =  mDatabase.rawQuery(query, null);

        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            Whisky whisky = new Whisky();

            whisky.setName(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_NAME)));
            whisky.setAge(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_AGE)));
            whisky.setStrength(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_STRENGTH)));
            whisky.setBottled(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_BOTTLED)));
            whisky.setBottler(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_BOTTLER)));
            whisky.setCaskNumber(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_CASK_NUMBER)));
            whisky.setRating(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_RATING)));
            whisky.setImageURL(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URL)));
            whisky.setDetailsURL(res.getString(res.getColumnIndex(DatabaseHelper.COLUMN_DETAILS_URL)));

            whiskies.add(whisky);

            res.moveToNext();
        }

        res.close();

        // finally return list of found whiskies
        return whiskies;
    }
}
