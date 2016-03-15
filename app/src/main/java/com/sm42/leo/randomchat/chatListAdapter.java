package com.sm42.leo.randomchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import chat.Chat;

/**
 * Created by Leo on 10-4-2015.
 */
public class chatListAdapter extends ArrayAdapter<Chat>
{
    private Context context;
    private ArrayList<Chat> chats;

    public chatListAdapter(Context context, ArrayList<Chat> chats)
    {
        super(context, R.layout.activity_chatlist, chats);
        this.context = context;
        this.chats = chats;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Chat requestedChat = chats.get(position);
        View chatListView = convertView;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            chatListView = inflater.inflate(R.layout.chatlistitem, null);
        }
        TextView userNameView = (TextView) chatListView.findViewById(R.id.chatname);
        userNameView.setText(requestedChat.getChatter());
        return chatListView;
    }
}
