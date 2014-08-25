package geese.dk.whiskysearch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.yelp.android.webimageview.WebImageView;

import java.util.ArrayList;
import java.util.List;

import geese.dk.whiskysearch.database.DatabaseHelper;
import geese.dk.whiskysearch.helpers.Whisky;

/**
 * Class responsible for showing a list of whiskies.
 * */
public class WhiskyListActivity extends ActionBarActivity
{
    private GridView mListView;

    /**
     * Called when the activity is created.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_whisky_list );

        mListView = (GridView) findViewById( R.id.whiskyListView );

        final ArrayList<Whisky> whiskyList = getIntent().getParcelableArrayListExtra( "whiskyArray" );

        WhiskyArrayAdapter adapter = new WhiskyArrayAdapter( this, whiskyList );

        mListView.setAdapter(adapter);
        
        mListView.setOnItemClickListener( new AdapterView.OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent(WhiskyListActivity.this, WhiskyDetailActivity.class);
                intent.putExtra("whiskyDetails", whiskyList.get(position));
                startActivity(intent);
			}
		} );
    }

    class WhiskyArrayAdapter extends ArrayAdapter<Whisky>
    {
        private final Context context;
        private final List<Whisky> whiskyList;

        public WhiskyArrayAdapter(Context context, List<Whisky> whiskyList)
        {
            super(context, R.layout.cell_whisky_list, whiskyList);
            this.context = context;
            this.whiskyList = whiskyList;
        }

        /**
         * Called when the cell in the gridview needs to be drawn.
         * */
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolderItem viewHolder;

            if(convertView == null)
            {
                // inflate the layout
                LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                convertView = inflater.inflate( R.layout.cell_whisky_list, parent, false );

                // set up the ViewHolder
                viewHolder = new ViewHolderItem();
                viewHolder.imageItem = (WebImageView) convertView.findViewById(R.id.icon);
                viewHolder.nameItem = (TextView) convertView.findViewById(R.id.name);
                viewHolder.bottlerItem = (TextView) convertView.findViewById(R.id.bottler);
                viewHolder.ageItem = (TextView) convertView.findViewById(R.id.age);
                viewHolder.ratingItem = (TextView) convertView.findViewById(R.id.rating);

                // store the holder with the view.
                convertView.setTag(viewHolder);
            }
            else
            {
                // we've just avoided calling findViewById() on resource every time
                // just use the viewHolder
                viewHolder = (ViewHolderItem) convertView.getTag();
            }

            // get whisky in question
            Whisky whisky =  whiskyList.get( position );

            // set cell data
            viewHolder.imageItem.setImageResource(R.drawable.bg_bottle);
            viewHolder.imageItem.setImageUrl(whisky.getImageURL());

            viewHolder.nameItem.setText(whisky.getName());

            viewHolder.bottlerItem.setText(whisky.getBottler());
            viewHolder.ageItem.setText("Age: " + whisky.getAge());
            viewHolder.ratingItem.setText(whisky.getRating() + "/100");

            // finally return cell
            return convertView;
        }
    }

    static class ViewHolderItem
    {
        WebImageView imageItem;
        TextView nameItem;
        TextView bottlerItem;
        TextView ageItem;
        TextView ratingItem;
    }
}