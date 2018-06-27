package com.kodonho.android.firebasechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kodonho.android.firebasechat.adapter.RoomListAdapter;
import com.kodonho.android.firebasechat.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class ChatList extends AppCompatActivity {
    public static final String ROOMS = "rooms";
    FirebaseDatabase database;
    DatabaseReference roomRef;

    RecyclerView recyclerView;
    LinearLayout layoutAdd;
    EditText editName;
    RadioGroup radioGroup;
    int limit = 2;

    RoomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        setAdapter();
        setView();
        setDatabase();
    }

    private void setAdapter(){
        adapter = new RoomListAdapter();
    }

    private void setView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        layoutAdd = findViewById(R.id.layoutAdd);
        editName = findViewById(R.id.editName);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radio2 : limit = 2; break;
                    case R.id.radio3 : limit = 3; break;
                    case R.id.radio4 : limit = 4; break;
                }
            }
        });
    }

    private void setDatabase() {
        database = FirebaseDatabase.getInstance();
        roomRef = database.getReference(ROOMS);
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Room> data = new ArrayList<>();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Room room = snapshot.getValue(Room.class);
                    data.add(room);
                }
                adapter.setDataAndRefresh(data);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void clickAdd(View view){
        if(layoutAdd.getVisibility() == View.GONE){
            layoutAdd.setVisibility(View.VISIBLE);
        }else if(layoutAdd.getVisibility() == View.VISIBLE){
            addRoom();
            layoutAdd.setVisibility(View.GONE);
        }
    }

    private void addRoom(){
        // 입력값을 데이터베이스에 등록
        String name = editName.getText().toString();
        // 키 생성
        String id = roomRef.push().getKey();
        Room room = new Room(id, name, limit);
        // 데이터베이스에 입력
        roomRef.child(id).setValue(room);
    }
}
