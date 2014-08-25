package geese.dk.whiskysearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import geese.dk.whiskysearch.helpers.Reachability;
import geese.dk.whiskysearch.helpers.Whisky;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

import com.yelp.android.webimageview.WebImageView;

public class WhiskyDetailActivity extends ActionBarActivity
{
	private static String OVERALL_RATING_VOTES;
	private static String OVERALL_RATING_RATING;

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
	                    	OVERALL_RATING_VOTES = content.childNodes().get(0).toString();
	                    	OVERALL_RATING_VOTES = OVERALL_RATING_VOTES.substring( 0, OVERALL_RATING_VOTES.indexOf( " " ) );
	                    }
	                    
	                    content = doc.getElementById( "whisky-rating" );
	                    
	                    if( content != null )
	                    {
	                    	OVERALL_RATING_RATING = content.childNodes().get(0).toString();
	                    	OVERALL_RATING_RATING = OVERALL_RATING_RATING.substring( 2, OVERALL_RATING_RATING.length() - 1 );
	                    }
	                    
	                    // Fetch the element that contains the 4 x 25 rating
	                    content = doc.getElementById( "whisky-average-4x25rating" );

                        if( content != null )
	                    {
	                    	// Remove the login warning
	                    	content.removeClass( "login-warning" );
	                    	
	                    	// If the content is now empty, then there are no reviews.
	                    	// Otherwise, fetch the reviews.
	                    }
	                    
	                    /*
	                    // Fetch the element that contains basic info about the whisky.
	                    Element content = doc.getElementById("whisky-info");
	
	                    String h = "HEJ!";
	                    
	                    if( content != null )
	                    {
	                        Elements elements = content.getElementsByTag("tr");
	                    	
                            h = h + "1";
	
	                        if( elements.size() > 0 )
	                        {

	                            // Iterate through them.
	                            for( Element e: elements )
	                            {


	                            }
	                        }
	                    }
	                    */
	                    
	                    String h = "HEJ!";
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
