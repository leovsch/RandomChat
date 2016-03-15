package chat;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by Leo on 16-4-2015.
 */
public class chatThread implements Runnable
{
    private ObjectInputStream stream;
    private ChatClient cc;
    private Administratie admin;

    public chatThread(ObjectInputStream stream)
    {
        this.stream = stream;
        admin = Administratie.getInstance();
    }

    @Override
    public void run()
    {
        while(!Thread.currentThread().isInterrupted())
        {
            try
            {
                Object object = stream.readObject();
                if(cc != null)
                {
                    if (object instanceof String)
                    {
                        String recievedString = (String) object;
                        if (recievedString.matches(".*\\d.*"))
                        {
                            cc.setDistance(recievedString);
                        }
                        else
                        {
                            cc.setOntvanger(recievedString);
                        }
                    }
                    if (object instanceof ChatMessage)
                    {
                        ChatMessage chatMessage = (ChatMessage) object;
                        cc.addRecievedMessage(chatMessage);
                        admin.addMessageToChat(chatMessage);
                    }
                }
            }
            catch (ClassNotFoundException | IOException e)
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setCc(ChatClient cc)
    {
        this.cc = cc;
    }
}
