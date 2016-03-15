package chat;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Leo on 17-4-2015.
 */
public class Administratie
{
    private ArrayList<Chat> chats;
    private static Administratie administratie;
    private boolean connected = false;
    private ObjectOutputStream internetOut;
    private ObjectInputStream internetIn;
    private ObjectOutputStream fileOut;
    private ObjectInputStream fileIn;
    private chatThread ct;


    private Administratie()
    {
        chats = new ArrayList<Chat>();
    }


    public ArrayList<Chat> getChats()
    {
        return chats;
    }

    public static Administratie getInstance()
    {
        if(administratie == null)
        {
            administratie = new Administratie();
        }
        return administratie;
    }

    private void startThread()
    {
        ct = new chatThread(internetIn);
        Thread t = new Thread(ct);
        t.start();
    }

    public void connectToServer(final ClientProfile profile)
    {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Socket s = new Socket("145.93.250.75", 8189);
                    OutputStream outStream = s.getOutputStream();
                    InputStream inStream = s.getInputStream();
                    // Let op: volgorde is van belang!
                    internetOut = new ObjectOutputStream(outStream);
                    internetIn = new ObjectInputStream(inStream);
                    internetOut.writeObject(profile);
                    internetOut.flush();
                    startThread();
                    connected = true;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }

    public boolean isConnected()
    {
        return connected;
    }

    public ObjectOutputStream getInternetOut() {
        return internetOut;
    }

    public ObjectInputStream getInternetIn() {
        return internetIn;
    }

    public void addChat(Chat chat)
    {
        chats.add(chat);
    }

    public chatThread getCt() {
        return ct;
    }

    public void addMessageToChat(ChatMessage message)
    {
        for(Chat c : chats)
        {
            if(c.getChatter().equals(message.getAfzender()) || c.getChatter().equals(message.getOntvanger()))
            {
                c.addMessage(message);
            }
        }
    }

    public void readChats(File file)
    {
        try
        {
            FileInputStream inputStream = new FileInputStream(file);
            fileIn = new ObjectInputStream(inputStream);
            this.chats = (ArrayList<Chat>)  fileIn.readObject();
            fileIn.close();
        }
        catch(IOException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public void writeChats(File file)
    {
        try
        {
            FileOutputStream inputStream = new FileOutputStream(file);
            fileOut = new ObjectOutputStream(inputStream);
            fileOut.writeObject(chats);
            fileOut.flush();
            fileOut.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
