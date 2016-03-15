package chat;

import android.os.AsyncTask;

import com.sm42.leo.randomchat.ActiveChat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Leo on 16-4-2015.
 */
public class ChatClient extends AsyncTask
{
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread t;
    private ClientProfile profile;
    private String ontvanger;
    private ActiveChat activeChat;
    private Administratie admin;
    private String niemandbescikbaar;

    public ChatClient(ClientProfile profile, ActiveChat activeChat)
    {
        this.profile = profile;
        this.activeChat = activeChat;
        admin = Administratie.getInstance();
        this.out = admin.getInternetOut();
        this.in = admin.getInternetIn();
        this.niemandbescikbaar = "Er is op dit moment geen client beschikbaar";
    }

    public void sendMessage(String message)
    {
        try
        {
            ChatMessage cm = new ChatMessage(message, profile.getName(), ontvanger);
            admin.addMessageToChat(cm);
            this.out.writeObject(cm);
            this.out.flush();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setDistance(String distance)
    {
        activeChat.addDistanceTextView(distance);
        Chat c = new Chat(ontvanger, distance);
        admin.addChat(c);
    }

    public void setOntvanger(String ontvanger)
    {
        activeChat.setChatter(ontvanger);

        if(!ontvanger.equals(niemandbescikbaar));
        {
            this.ontvanger = ontvanger;

        }
    }

    public void setOldOntvanger(String ontvanger)
    {
        this.ontvanger = ontvanger;
    }

    public ClientProfile getProfile()
    {
        return profile;
    }

    public void addRecievedMessage(ChatMessage message)
    {
        activeChat.addRecievedMessage(message.getBericht());
    }

    @Override
    protected Object doInBackground(Object[] params)
    {
        try
        {
            this.out.writeObject("nieuwerandomchat");
            this.out.flush();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}