package com.kodonho.android.firebasechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatRoom extends AppCompatActivity {
    public static final String ROOM_ID = "room_id";
    String room_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Intent intent = getIntent();
        room_id = intent.getStringExtra(ROOM_ID);
        // 다시 파이어베이스 데이터베이스에서, room_id에 해당하는 채팅 목록을 가져온다.
    }
}
