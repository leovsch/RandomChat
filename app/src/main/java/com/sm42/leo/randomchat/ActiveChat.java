package com.sm42.leo.randomchat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import chat.Administratie;
import chat.Chat;
import chat.ChatClient;
import chat.ChatMessage;
import chat.ClientProfile;


public class ActiveChat extends Activity {

    private ChatClient cc;
    private Administratie admin;
    private RelativeLayout messagesLayout;
    private int lastID;
    private EditText messageField;
    private ScrollView svMessages;
    private TextView activeChatterView;
    private int usedid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_chat);
        activeChatterView = (TextView) findViewById(R.id.tvChatter);
        messagesLayout = (RelativeLayout) findViewById(R.id.layoutMessages);
        messageField = (EditText) findViewById(R.id.tfMessage);
        svMessages = (ScrollView) findViewById(R.id.svMessages);
        admin = Administratie.getInstance();
        Intent intent = getIntent();
        ClientProfile profile = (ClientProfile) intent.getSerializableExtra("Profile");
        cc = new ChatClient(profile, this);
        int index = intent.getIntExtra("chatIndex", -1);
        if(admin.isConnected())
        {
            admin.getCt().setCc(cc);
        }
        if(index >= 0)
        {
            Chat c = admin.getChats().get(index);
            this.setChatter(c.toString());
            for(ChatMessage cm : c.getMessages())
            {
                if(cm.getAfzender().equals(profile.getName()))
                {
                    addSelfMessage(cm.getBericht());
                }
                else
                {
                    addRecievedMessage(cm.getBericht());
                }
            }
            cc.setOldOntvanger(c.getChatter());
        }
        else if(admin.isConnected())
        {
            cc.execute();
        }
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        if(admin.isConnected()) {
            admin.getCt().setCc(null);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, ChatList.class);
        intent.putExtra("username", cc.getProfile().getName());
        intent.putExtra("interest", cc.getProfile().getInterest());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_active_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public void btnSend_Click(View view)
    {
        String message = messageField.getText().toString();
        messageField.getText().clear();
        addSelfMessage(message);
        cc.sendMessage(message);
    }

    public void addSelfMessage(String message)
    {
        TextView textView = new TextView(new ContextThemeWrapper(ActiveChat.this, R.style.chatMessageStyle));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.BELOW, lastID);
        params.topMargin = 5;
        textView.setLayoutParams(params);
        textView.setId(generateId());
        textView.setText(message);
        svMessages.fullScroll(View.FOCUS_DOWN);
        messagesLayout.addView(textView);
        lastID = textView.getId();
    }

    public void addRecievedMessage(final String message)
    {
        runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                TextView textView = new TextView(new ContextThemeWrapper(ActiveChat.this, R.style.chatMessageStyle));
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                params.addRule(RelativeLayout.BELOW, lastID);
                params.topMargin = 5;
                textView.setLayoutParams(params);
                textView.setId(generateId());
                textView.setText(message);
                svMessages.fullScroll(View.FOCUS_DOWN);
                messagesLayout.addView(textView);
                lastID = textView.getId();
            }
        });
    }

    public void setChatter(final String chatter)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activeChatterView.setText(chatter);
            }
        });
    }

    public void addDistanceTextView(final String distance)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activeChatterView.append(" afstand: " + distance + "km");
            }
        });
    }

    private int generateId()
    {
        usedid = usedid + 1;
        return usedid;
    }
}
