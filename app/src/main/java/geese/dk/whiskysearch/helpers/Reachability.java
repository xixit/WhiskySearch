package geese.dk.whiskysearch.helpers;

import java.net.InetAddress;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;


/// <summary>
/// Class responsible for answering several network related questions:
/// 
/// Q1: do we have network access (whether by WiFi or other)?
/// Q2: can we get in contact with a given host?
/// Q3: can we get in contact with a specific test host? (for debug purposes really)
/// 
/// All three questions will be answered with a boolean.
/// True for yes, false for no.
/// </summary>
public class Reachability
{
    // the tag used for any possible thrown error messages
    private static final String TAG = "WhiskySearch - Reachability";

    /// <summary>
    /// Test host for answering Q3, is set to www.google.com
    /// </summary>
    private static final String HostName = "www.google.com";

    /// <summary>
    /// bool for saying if there's net connection using 3G or other
    /// </summary>
    private static boolean cMobile = false;

    /// <summary>
    /// bool for saying if there's net connection using WiFi
    /// </summary>
    private static boolean cWifi = false;

    /// <summary>
    /// Checks if ANY network access is available.
    /// </summary>
    /// <param name="context">Context used for getting ConnectivityManager object</param>
    /// <returns>True if ANY network is available, whether it be WiFi or mobile, false if not. Answers Q1.</returns>
    public static boolean isNetworkAvailable(Context context)
    {
        try
        {
            ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

            try
            {
                cMobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected();
            }
            catch(Exception ex)
            {
                cMobile = false;
            }

            try
            {
                cWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
            }
            catch (Exception ex)
            {
                cWifi = false;
            }

            // if mobile network is available return mobile (true), else return wifi
            return (cMobile) ? cMobile : cWifi;
        }
        catch(Exception ex)
        {
        	Log.e( TAG, ex.getMessage() );

            return false;
        }
    }

    /// <summary>
    /// Checks if a generic host is available within a given time.
    /// </summary>
    /// <returns>True if the generic host is available, false if not available, there was a timeout, or an exception was thrown. Answers Q2.</returns>
    public static boolean isHostReachable()
    {
        return isHostReachable(HostName);
    }

    /// <summary>
    /// Checks if a specific host is available within a given time.
    /// </summary>
    /// <returns>Returns true if the specific host is available, false if not available, there was a timeout, or an exception was thrown. Answers Q3.</returns>
    public static boolean isHostReachable(String host)
    {
        try
        {
            return InetAddress.getAllByName(host).length > 0;
        }
        catch (Exception ex)
        {
        	Log.e( TAG, ex.getMessage() );

            return false;
        }
    }
}

