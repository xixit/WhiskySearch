package geese.dk.whiskysearch.helpers;

/**
 * Created by f2k on 23/05/15.
 */
public class TastingNote extends Note
{
    protected String mColour;
    protected String mNose;
    protected String mTaste;
    protected String mFinish;
    protected String mComments;

    public TastingNote()
    {}

    public TastingNote( String author, String date, String rating, String note,
                        String colour, String nose, String taste, String finish, String comments )
    {
        super( author, date, rating, note );
        mColour = colour;
        mNose = nose;
        mTaste = taste;
        mFinish = finish;
        mComments = comments;
    }

    public String getColour()
    {
        return mColour;
    }

    public void setColour(String colour)
    {
        this.mColour = colour;
    }

    public String getNose()
    {
        return mNose;
    }

    public void setNose(String nose)
    {
        this.mNose = nose;
    }

    public String getTaste()
    {
        return mTaste;
    }

    public void setTaste(String taste)
    {
        this.mTaste = taste;
    }

    public String getFinish()
    {
        return mFinish;
    }

    public void setFinish(String finish)
    {
        this.mFinish = finish;
    }

    public String getComments()
    {
        return mComments;
    }

    public void setComments(String comments)
    {
        this.mComments = comments;
    }
}
