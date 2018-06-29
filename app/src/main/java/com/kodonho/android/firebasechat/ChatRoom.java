package com.kodonho.android.firebasechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kodonho.android.firebasechat.adapter.ChatListAdapter;
import com.kodonho.android.firebasechat.domain.Chat;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {
    public static final String ROOM_ID = "room_id";
    public static final String ROOM_NAME = "room_name";
    public static final String USER_ID = "user_id";

    public static final String REF_CHAT_ROOT = "chat";

    String room_id,room_name,user_id;

    TextView textTitle;
    RecyclerView recyclerView;
    ChatListAdapter adapter;
    EditText editMsg;

    FirebaseDatabase database;
    DatabaseReference currentRoomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setView();
        init();
        setAdapter();
        setFirebase();
    }

    private void init(){
        Intent intent = getIntent();
        room_id = intent.getStringExtra(ROOM_ID);
        room_name = intent.getStringExtra(ROOM_NAME);
        user_id = intent.getStringExtra(USER_ID);
        textTitle.setText("Room : "+room_name+"("+room_id+")");
    }

    private void setView(){
        textTitle = findViewById(R.id.textTitle);
        recyclerView = findViewById(R.id.recyclerView);
        editMsg = findViewById(R.id.editMsg);
    }
    private void setAdapter(){
        adapter = new ChatListAdapter(user_id);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    private void setFirebase(){
        database = FirebaseDatabase.getInstance();
        currentRoomRef = database.getReference(REF_CHAT_ROOT);
        currentRoomRef.child(room_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    data.add(chat);
                }
                adapter.setDataAndRefresh(data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        chat.user_id = user_id;
        chat.timestamp = System.currentTimeMillis();
        return chat;
    }
}


