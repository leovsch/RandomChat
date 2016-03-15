package chat;

import java.io.Serializable;

/**
 * Created by Leo on 16-4-2015.
 */
public class ClientProfile implements Serializable
{
    private static final long serialVersionUID = -7480282314584874590L;
    private String name;
    private String interest;
    private double longitude;
    private double latidude;

    public ClientProfile(String name, String interest, double longitude, double latidude)
    {
        this.name = name;
        this.interest = interest;
        this.longitude = longitude;
        this.latidude = latidude;
    }

    public String getInterest()
    {
        return interest;
    }

    public String getName()
    {
        return name;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatidude() {
        return latidude;
    }
}
