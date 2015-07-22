package geese.dk.whiskysearch;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import geese.dk.whiskysearch.helpers.Note;
import geese.dk.whiskysearch.helpers.Reachability;
import geese.dk.whiskysearch.helpers.TastingNote;
import geese.dk.whiskysearch.helpers.Whisky;
import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
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
                                    // Find the node containing the note.
                                    Node childNode = child.childNode(3);

                                    // Extract the text containing the author.
                                    String author = childNode.childNode( 1 ).childNode( 1 ).toString();
                                    // Extract the name of the author.
                                    author = author.substring(author.indexOf("\">") + 2, author.length() - 4);

                                    // Extract the text containing the date.
                                    String date = childNode.childNode( 1 ).childNode( 5 ).toString();
                                    // Remove part of the string.
                                    // This is needed in order to find the correct substring.
                                    date = date.replace("<dic class=\"note-rating\">", "");
                                    // Extract the date.
                                    date = date.substring(date.indexOf("title=\"") + 7, date.indexOf("\">"));

                                    // Extract the rating.
                                    String rating = childNode.childNode( 1 ).childNode( 5 ).toString();
                                    // Extract the rating.
                                    rating = rating.substring(rating.indexOf("<b>") + 3, rating.indexOf("</b>"));

                                    String note = childNode.childNode( 3 ).childNode( 1 ).toString();
                                    note = Html.fromHtml( note ).toString();

                                    mWhisky.setNote( new Note( author, date, rating, note ) );
                                }
                                // If the child has attributes and the attribute contains "whisky-note-taste" it's a tasting note.
                                else if( child.attributes() != null && child.attributes().toString().contains("whisky-note-tasting") )
                                {
                                    // Find the node containing the note.
                                    Node childNode = child.childNode(3);

                                    // Extract the text containing the author.
                                    String author = childNode.childNode( 1 ).childNode( 1 ).toString();
                                    // Extract the name of the author.
                                    author = author.substring(author.indexOf("\">") + 2, author.length() - 4);

                                    // Extract the text containing the date.
                                    String date = childNode.childNode( 1 ).childNode( 5 ).toString();
                                    // Remove part of the string.
                                    // This is needed in order to find the correct substring.
                                    date = date.replace("<dic class=\"note-rating\">", "");
                                    // Extract the date.
                                    date = date.substring(date.indexOf("title=\"") + 7, date.indexOf("\">"));

                                    // Extract the rating.
                                    String rating = childNode.childNode( 1 ).childNode( 5 ).toString();
                                    // Extract the rating.
                                    rating = rating.substring(rating.indexOf("<b>") + 3, rating.indexOf("</b>"));

                                    String note = "";

                                    String colour = childNode.childNode( 3 ).childNode( 1 ).childNode( 1 ).childNode( 3 ).childNode( 2 ).toString();

                                    String nose = childNode.childNode(3).childNode( 1 ).childNode(3).childNode(3).toString();
                                    nose = Html.fromHtml( nose ).toString();

                                    String taste = childNode.childNode(3).childNode( 1 ).childNode(5).childNode(3).toString();
                                    taste = Html.fromHtml( taste ).toString();

                                    String finish = childNode.childNode(3).childNode( 1 ).childNode(7).childNode(3).toString();
                                    finish = Html.fromHtml( finish ).toString();

                                    String comments = childNode.childNode(3).childNode( 1 ).childNode(9).childNode(3).toString();
                                    comments = Html.fromHtml( comments ).toString();

                                    mWhisky.setNote( new TastingNote( author, date, rating, note, colour, nose, taste, finish, comments ) );
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
