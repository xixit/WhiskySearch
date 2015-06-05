package geese.dk.whiskysearch.helpers;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by f2k on 18-07-2014.
 */
public class Whisky implements Parcelable
{
    private String mName;
    private String mAge;
    private String mStrength;
    private String mBottled;
    private String mBottler;
    private String mCaskNumber;
    private String mRating;
    private String mNumberOfVotes;
    private String mImageURL;
    private String mDetailsURL;
    private ArrayList<Note> mNotes;

    public Whisky()
    {
        mNotes = new ArrayList<Note>();
    }

    public Whisky( String name, String age, String strength, String bottled, String bottler, String caskNumber, String rating, String imageURL, String detailsURL )
    {
        mName = name;
        mAge = age;
        mStrength = strength;
        mBottled = bottled;
        mBottler = bottler;
        mCaskNumber = caskNumber;
        mRating = rating;
        mNumberOfVotes = "";
        mImageURL = imageURL;
        mDetailsURL = detailsURL;
        mNotes = new ArrayList<Note>();
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getAge()
    {
        return mAge;
    }

    public void setAge(String age) { mAge = age; }

    public String getStrength()
    {
        return mStrength;
    }

    public void setStrength(String strength)
    {
        mStrength = strength;
    }

    public String getBottled()
    {
        return mBottled;
    }

    public void setBottled(String bottled)
    {
        mBottled = bottled;
    }

    public String getBottler()
    {
        return mBottler;
    }

    public void setBottler(String bottler)
    {
        mBottler = bottler;
    }

    public String getCaskNumber()
    {
        return mCaskNumber;
    }

    public void setCaskNumber(String caskNumber)
    {
        mCaskNumber = caskNumber;
    }

    public String getRating()
    {
        return mRating;
    }

    public void setRating(String rating)
    {
        mRating = rating;
    }

    public String getNumberOfVotes()
    {
        return mNumberOfVotes;
    }

    public void setNumberOfVotes(String numberOfVotes)
    {
        mNumberOfVotes = numberOfVotes;
    }

    public String getImageURL()
    {
        return mImageURL;
    }

    public void setImageURL(String imageURL)
    {
        mImageURL = imageURL;
    }

    public String getDetailsURL()
    {
        return mDetailsURL;
    }

    public void setDetailsURL(String detailsURL)
    {
        mDetailsURL = detailsURL;
    }

    public ArrayList<Note> getNotes()
    {
        return mNotes;
    }

    public void setNote(Note note)
    {
        mNotes.add( note );
    }

    public void setNotes(ArrayList<Note> notes)
    {
        mNotes = notes;
    }

    public static final Parcelable.Creator<Whisky> CREATOR = new Creator<Whisky>()
    {
        @Override
        public Whisky createFromParcel(Parcel source)
        {
            Whisky w = new Whisky();

            w.setName( source.readString() );
            w.setAge( source.readString() );
            w.setStrength( source.readString() );
            w.setBottled( source.readString() );
            w.setBottler( source.readString() );
            w.setCaskNumber( source.readString() );
            w.setRating( source.readString() );
            w.setNumberOfVotes( source.readString() );
            w.setImageURL( source.readString() );
            w.setDetailsURL( source.readString() );
            w.setNotes( source.readArrayList( Note.class.getClassLoader() ) );

            return w;
        }

        @Override
        public Whisky[] newArray(int size)
        {
            return new Whisky[size];
        }
    };

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(mName);
        dest.writeString(mAge);
        dest.writeString(mStrength);
        dest.writeString(mBottled);
        dest.writeString(mBottler);
        dest.writeString(mCaskNumber);
        dest.writeString(mRating);
        dest.writeString(mNumberOfVotes);
        dest.writeString(mImageURL);
        dest.writeString(mDetailsURL);
        dest.writeList(mNotes);
    }
}