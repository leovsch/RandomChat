package chat;

import java.io.Serializable;

/**
 * Created by Leo on 10-4-2015.
 */
public class ChatMessage implements Serializable
{
    private static final long serialVersionUID = -7480282314589874590L;
    private String bericht;
    private String afzender;
    private String ontvanger;

    public ChatMessage(String bericht, String afzender, String ontvanger)
    {
        this.bericht = bericht;
        this.afzender = afzender;
        this.ontvanger = ontvanger;
    }

    public String getOntvanger()
    {
        return ontvanger;
    }

    public String getBericht()
    {
        return bericht;
    }

    public String getAfzender() { return afzender; }

    @Override
    public String toString()
    {
        return "[" + afzender + "]: " + bericht;
    }
}
