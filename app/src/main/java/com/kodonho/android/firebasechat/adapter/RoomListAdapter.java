package com.kodonho.android.firebasechat.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kodonho.android.firebasechat.ChatRoom;
import com.kodonho.android.firebasechat.R;
import com.kodonho.android.firebasechat.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.Holder>{
    List<Room> data = new ArrayList<>();
    String user_id;
    public RoomListAdapter(String user_id){
        this.user_id = user_id;
    }
    public void setDataAndRefresh(List<Room> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.roomlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Room room = data.get(position);
        holder.setRoom(room);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView textName;
        Room room;
        public Holder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ChatRoom.class);
                    intent.putExtra(ChatRoom.ROOM_ID,room.id);
                    intent.putExtra(ChatRoom.ROOM_NAME,room.name);
                    intent.putExtra(ChatRoom.USER_ID, user_id);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void setRoom(Room room) {
            this.room = room;
            textName.setText(room.name);
        }
    }
}
