package geese.dk.whiskysearch.helpers;

/**
 * Created by f2k on 23/05/15.
 */
public class Note
{
    protected String mAuthor;
    protected String mDate;
    protected String mRating;
    protected String mNote;

    public Note()
    {}

    public Note( String author, String date, String rating, String note )
    {
        mAuthor = author;
        mDate = date;
        mRating = rating;
        mNote = note;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public void setAuthor(String author)
    {
        this.mAuthor = author;
    }

    public String getDate()
    {
        return mDate;
    }

    public void setDate(String date)
    {
        this.mDate = date;
    }

    public String getRating()
    {
        return mRating;
    }

    public void setRating(String rating)
    {
        this.mRating = rating;
    }

    public String getNote()
    {
        return mNote;
    }

    public void setNote(String note)
    {
        this.mNote = note;
    }
}
