package geese.dk.whiskysearch.helpers;

public class WhiskySearch
{
    private String mDistillery;
    private String mName;
    private String mAge;
    private String mBottled;
    private String mBottler;

    public WhiskySearch()
    {
        mDistillery = "";
        mName = "";
        mAge = "";
        mBottled = "";
        mBottler = "";
    }

    public WhiskySearch(String distillery, String name, String age, String bottled, String bottler)
    {
        mDistillery = distillery;
        mName = name;
        mAge = age;
        mBottled = bottled;
        mBottler = bottler;
    }

    public String getDistillery()
    {
        return mDistillery;
    }

    public void setDistillery(String distillery)
    {
        this.mDistillery = distillery;
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        this.mName = name;
    }

    public String getAge()
    {
        return mAge;
    }

    public void setAge(String age)
    {
        this.mAge = age;
    }

    public String getBottled()
    {
        return mBottled;
    }

    public void setBottled(String bottled)
    {
        this.mBottled = bottled;
    }

    public String getBottler()
    {
        return mBottler;
    }

    public void setBottler(String bottler)
    {
        this.mBottler = bottler;
    }
}
