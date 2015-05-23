package geese.dk.whiskysearch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.yelp.android.webimageview.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import geese.dk.whiskysearch.database.DatabaseHelper;
import geese.dk.whiskysearch.database.WhiskyDataSource;
import geese.dk.whiskysearch.helpers.Reachability;
import geese.dk.whiskysearch.helpers.Whisky;
import geese.dk.whiskysearch.helpers.WhiskySearch;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;

public class SearchActivity extends ActionBarActivity
{
    private EditText mEditDistillery, mEditName, mEditAge, mEditBottled, mEditBottler;

    private WhiskySearch mSearch;

    //private WhiskyDataSource mDatasource;

    /**
     * Called when the activity is created.
     * */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

        //mDatasource = new WhiskyDataSource(this);
        //mDatasource.open();

        // initializes the image loader for the web image view
        ImageLoader.initialize(this, null);

        mEditDistillery = (EditText)findViewById(R.id.edit_distillery);
        mEditName = (EditText)findViewById(R.id.edit_name);
        mEditAge = (EditText)findViewById(R.id.edit_age);
        mEditBottled = (EditText)findViewById(R.id.edit_bottled);
        mEditBottler = (EditText)findViewById(R.id.edit_bottler);
	}

    @Override
    protected void onResume()
    {
        //mDatasource.open();
        super.onResume();
    }

    @Override
    protected void onPause()
    {
        //mDatasource.close();
        super.onPause();
    }

	/**
     * Called when the search button is pressed.
     * Will start searching for whiskies with the given input.
     * @param view
     */
    public void onBtnSearch(View view)
    {
        final CheckBox checkBox = (CheckBox)findViewById(R.id.checkBox);

        final ProgressDialog ringProgressDialog = ProgressDialog.show(SearchActivity.this, "Please wait ...", "Downloading data ...", true);
        ringProgressDialog.setCancelable(true);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                final ArrayList<Whisky> whiskyList = new ArrayList<Whisky>();

                try
                {
                	// Check if a network is available.
                	//if( Reachability.isNetworkAvailable( SearchActivity.this ) )
                    if(!checkBox.isChecked())
                	{               	
	                    // create search
                        Document doc = Jsoup.connect( getSearchString() ).get();

                        Element content = doc.getElementById("tab-whsk");

	                    if( content != null )
	                    {
	                        Elements contents = content.getElementsByTag("tbody");
	
	                        if( contents.size() > 0 )
	                        {
	                            Element e = contents.get(0);
	
	                            // This is the rows.
	                            Elements children = e.children();
	
	                            // Iterate through them.
	                            for(Element child: children)
	                            {
                                    if( child.childNodes().size() >= 18 )
                                    {

                                        Whisky whisky = new Whisky();

                                        // Get the full string from the attributes (ugly solution, but only way I can see)
                                        String detailsString = child.childNode(5).childNode(1).attributes().html();

                                        // Strip the full string of the beginning and end.
                                        detailsString = detailsString.substring(7, detailsString.length() - 1);

                                        whisky.setDetailsURL(detailsString);

                                        // Find the URL for the image.
                                        // NOTE: not all entries will have an image.

                                        if (child.childNode(3).childNodes().size() > 1)
                                        {
                                            // Get the full string from the attributes (ugly solution, but only way I can see)
                                            String imageString = child.childNode(3).childNode(1).attributes().asList().get(2).html();

                                            // Strip the full string of the beginning and end.
                                            imageString = imageString.substring(12, imageString.length() - 1);

                                            whisky.setImageURL(imageString);
                                        }
                                        else
                                        {
                                            whisky.setImageURL(null);
                                        }

                                        whisky.setName(((TextNode) (child.childNode(5).childNode(1).childNode(0))).text());
                                        whisky.setAge(((TextNode) (child.childNode(7).childNode(1).childNode(0))).text());
                                        whisky.setStrength(((TextNode) (child.childNode(9).childNode(0).childNode(0))).text());
                                        whisky.setBottled(((TextNode) (child.childNode(11).childNode(0).childNode(0))).text());
                                        whisky.setCaskNumber(((TextNode) (child.childNode(13).childNode(0).childNode(0))).text());
                                        whisky.setBottler(((TextNode) (child.childNode(15).childNode(0).childNode(0))).text());
                                        whisky.setRating(((TextNode) (child.childNode(17).childNode(0).childNode(0))).text());

                                        whiskyList.add(whisky);

                                        // and save whisky to DB
                                        //mDatasource.addWhisky(whisky);
                                        DatabaseHelper.instance(SearchActivity.this).addWhisky(whisky);
                                    }
	                            }
	                        }
	                    }
                	}
                    // if no net is available
                    else
                    {
                        // get whiskies from database instead
                        //ArrayList<Whisky> whiskies = mDatasource.getWhiskies(mSearch);
                        ArrayList<Whisky> whiskies = DatabaseHelper.instance(SearchActivity.this).getWhiskies(mSearch);

                        for(Whisky whisky : whiskies)
                        {
                            whiskyList.add(whisky);
                        }
                    }
                }
                catch (Exception ex)
                {
                    Log.v("SearchActivity", ex.getMessage() + " - " + ex.getStackTrace());
                    System.out.println(ex.getMessage() + " - " + ex.getStackTrace());
                }
                
                ringProgressDialog.dismiss();

//                // TEST: save all whiskies to DB
//                for(Whisky whisky : whiskyList)
//                {
//                    DatabaseHelper.instance(SearchActivity.this).addWhisky(whisky);
//                }
//
//                // TEST: get all whiskies from search
//                ArrayList<Whisky> whiskies = DatabaseHelper.instance(SearchActivity.this).getWhiskies(mSearch);
//
//                // TEST: and print them out
//                for(Whisky whisky : whiskies)
//                {
//                    Log.v("SearchActivity", whisky.getName());
//                    System.out.println(whisky.getName());
//                }

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Intent intent = new Intent(SearchActivity.this, WhiskyListActivity.class);
                        intent.putParcelableArrayListExtra( "whiskyArray", whiskyList );
                        startActivity(intent);
                    }
                });
            }
        }).start();
    }

    /**
     * Returns search string for Whiskybase
     * @return
     */
    private String getSearchString()
    {
        // TODO: optimize using StringBuilder or something

        String search = "http://whiskybase.com/search?f=";

        String distillery = mEditDistillery.getText().toString();
        if(distillery != null && !distillery.isEmpty())
            search += "+" + distillery;
        else
            distillery = "";

        String name = mEditName.getText().toString();
        if(name != null && !name.isEmpty())
            search += "+" + name;
        else
            name = "";

        String age = mEditAge.getText().toString();
        if(age != null && !age.isEmpty())
            search += "+age:" + age;
        else
            age = "";

        String bottled = mEditBottled.getText().toString();
        if(bottled != null && !bottled.isEmpty())
            search += "+bottled:" + bottled;
        else
            bottled = "";

        String bottler = mEditBottler.getText().toString();
        if(bottler != null && !bottler.isEmpty())
            search += "+" + bottler;
        else
            bottler = "";

        // set search result (for DB use)
        mSearch = new WhiskySearch(distillery, name, age, bottled, bottler);

        // return search string
        return search;
    }
}
