package chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Leo on 10-4-2015.
 */
public class Chat implements Serializable
{
    private ArrayList<ChatMessage> messages;
    private String afstand;
    private String chatter;

    public Chat(String chatter, String afstand)
    {
        this.chatter = chatter;
        messages = new ArrayList<ChatMessage>();
        this.afstand = afstand;
    }

    public String getChatter()
    {
        return chatter;
    }

    public ArrayList<ChatMessage> getMessages()
    {
        return this.messages;
    }

    public void addMessage(ChatMessage message)
    {
        messages.add(message);
    }

    @Override
    public String toString()
    {
        return chatter + " " + afstand + "km";
    }
}
