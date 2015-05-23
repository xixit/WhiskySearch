package geese.dk.whiskysearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import geese.dk.whiskysearch.helpers.Reachability;
import geese.dk.whiskysearch.helpers.Whisky;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.yelp.android.webimageview.WebImageView;

import java.util.Set;

public class WhiskyDetailActivity extends ActionBarActivity
{
    // whisky to get and show info about
    private Whisky mWhisky;

    // image view for showing the whisky in
    private WebImageView mImage;

    // text views for showing detailed info about the whisky
    private TextView mTextName, mTextBottler, mTextBottled, mTextStrength, mTextCaskNumber, mTextAge, mTextRating;

    /**
     * Called when the activity is created.
     * */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_whisky_detail);
		
		mWhisky = getIntent().getParcelableExtra( "whiskyDetails" );

        // get the web image view
        mImage = (WebImageView) findViewById( R.id.image );

        // get the text views
        mTextName = (TextView) findViewById( R.id.name );
        mTextBottler = (TextView) findViewById( R.id.bottler );
        mTextBottled = (TextView) findViewById( R.id.bottled );
        mTextStrength = (TextView) findViewById( R.id.strength );
        mTextCaskNumber = (TextView) findViewById( R.id.cask_number );
        mTextAge = (TextView) findViewById( R.id.age );
        mTextRating = (TextView) findViewById( R.id.rating );
	}

    /**
     * Called when the view gains focus.
     * */
	protected void onResume()
	{
		super.onResume();
		
		search();
	}

    /**
     * Will find more detailed information about the given whisky, and then show the information.
     * */
	private void search()
	{
		final ProgressDialog ringProgressDialog = ProgressDialog.show(WhiskyDetailActivity.this, "Please wait ...", "Downloading data ...", true);
        ringProgressDialog.setCancelable(true);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                try
                {
                	// Check if a network is available.
                	if( Reachability.isNetworkAvailable( WhiskyDetailActivity.this ) )
                	{               	
	                    // Fetch the data
	                    Document doc = Jsoup.connect( mWhisky.getDetailsURL() ).get();
	
	                    // Check this link for an example of a page with lots of details:
	                    // http://whiskybase.com/whisky/43906
	                    
	                    
	                    // Fetch the elements that contains the overall rating.
	                    
	                    Element content = doc.getElementById( "whisky-rating-text" );
	                    
	                    if( content != null )
	                    {
	                    	mWhisky.setNumberOfVotes( content.childNodes().get(0).toString());
                            mWhisky.setNumberOfVotes( mWhisky.getNumberOfVotes().substring(0, mWhisky.getNumberOfVotes().indexOf(" ") ) );
	                    }
	                    
	                    content = doc.getElementById( "whisky-rating" );
	                    
	                    if( content != null )
	                    {
                            mWhisky.setRating( content.childNodes().get(0).toString() );
                            mWhisky.setRating( mWhisky.getRating().substring( 2, mWhisky.getRating().length() - 1 ) );
	                    }

	                    // Fetch the element that contains basic info about the whisky.
	                    content = doc.getElementById("whisky-notes");
	                    
	                    if( content != null )
	                    {
                            for( Element child: content.children() )
                            {
                                // If the child has attributes and the attribute contains "whisky-note-note" it's a note.
                                if( child.attributes() != null && child.attributes().toString().contains("whisky-note-note") )
                                {
                                    String s = "";
                                }
                            }

                            String s = "";
	                    }

                	}

                }
                catch (Exception e)
                {
                    String sdf = "";
                }
                
                ringProgressDialog.dismiss();

                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        // finally set the whisky with info
                        setWhisky();
                    }
                });
            }
        }).start();
	}

    /**
     * Will show detailed information about the whisky in the activity.
     * */
    private void setWhisky()
    {
        mImage.setImageResource(R.drawable.bg_bottle);
        mImage.setImageUrl(mWhisky.getImageURL());

        mTextName.setText(mWhisky.getName());
        mTextBottler.setText("Bottler: " + mWhisky.getBottler());
        mTextBottled.setText("Bottled: " + mWhisky.getBottled());
        mTextStrength.setText("Strength: " + mWhisky.getStrength());
        mTextCaskNumber.setText("Cask number: " + mWhisky.getCaskNumber());
        mTextAge.setText("Age: " + mWhisky.getAge());
        mTextRating.setText("Rating: " + mWhisky.getRating() + "/100");
    }
}
