package com.sm42.leo.randomchat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;

import chat.Administratie;
import chat.Chat;
import chat.ChatMessage;
import chat.ClientProfile;


public class ChatList extends Activity {

    private ClientProfile profile;
    private Administratie administratie;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        listView = (ListView) findViewById(R.id.lvChats);
        Intent intent = getIntent();
        profile = new ClientProfile(intent.getStringExtra("username"), intent.getStringExtra("interest"), intent.getDoubleExtra("longitude", 0.0), intent.getDoubleExtra("latidude", 0.0));
        administratie = Administratie.getInstance();
        if(administratie.isConnected() == false)
        {
            administratie.connectToServer(profile);
        }
        chatListAdapter chatlistadapter = new chatListAdapter(getApplicationContext(), administratie.getChats());
        listView.setAdapter(chatlistadapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(getApplicationContext(), ActiveChat.class);
                intent.putExtra("Profile", profile);
                intent.putExtra("chatIndex", position);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatlist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_chat)
        {
            itemNewChat_Click();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        File file = new File(getApplicationContext().getFilesDir(), "chats");
        administratie.writeChats(file);
    }

    private void itemNewChat_Click()
    {
        Intent intent = new Intent(this, ActiveChat.class);
        intent.putExtra("Profile", profile);
        intent.putExtra("chatIndex", -1);
        startActivity(intent);
        finish();
    }
}
