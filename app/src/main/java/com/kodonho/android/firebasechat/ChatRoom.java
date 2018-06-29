package com.kodonho.android.firebasechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kodonho.android.firebasechat.domain.Chat;

public class ChatRoom extends AppCompatActivity {
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";

    public static final String REF_CHAT_ROOT = "chat";

    String room_id,room_name;

    TextView textTitle;
    RecyclerView recyclerView;
    EditText editMsg;

    FirebaseDatabase database;
    DatabaseReference currentRoomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setView();
        setFirebase();
        init();
    }

    private void init(){
        Intent intent = getIntent();
        room_id = intent.getStringExtra(ROOM_ID);
        room_name = intent.getStringExtra(ROOM_NAME);
        textTitle.setText("Room : "+room_name+"("+room_id+")");
    }

    private void setView(){
        textTitle = findViewById(R.id.textTitle);
        recyclerView = findViewById(R.id.recyclerView);
        editMsg = findViewById(R.id.editMsg);

    }
    private void setFirebase(){
        database = FirebaseDatabase.getInstance();
        currentRoomRef = database.getReference(REF_CHAT_ROOT);
    }

    public void clickSend(View view){
        String msg = editMsg.getText().toString();
        sendMsg(msg);
    }

    private void sendMsg(String msg){
        Chat chat = makeMessage(msg);
        String key = currentRoomRef.child(room_id).push().getKey();
        currentRoomRef.child(room_id).child(key).setValue(chat);
    }

    private Chat makeMessage(String msg){
        Chat chat = new Chat();
        chat.msg = msg;
        chat.user_id = "aaa";
        chat.timestamp = System.currentTimeMillis();
        return chat;
    }
}


