package com.kodonho.android.firebasechat.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kodonho.android.firebasechat.ChatRoom;
import com.kodonho.android.firebasechat.R;
import com.kodonho.android.firebasechat.domain.Chat;
import com.kodonho.android.firebasechat.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Holder>{
    String user_id = "";
    List<Chat> data = new ArrayList<>();
    public ChatListAdapter(String user_id){
        this.user_id = user_id;
    }
    public void setDataAndRefresh(List<Chat> data){
        this.data = data;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.chatlist_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Chat chat = data.get(position);
        holder.setChat(chat);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        LinearLayout layout;
        TextView textName,textMsg,textTime;
        Chat chat;
        public Holder(View itemView) {
            super(itemView);
            layout = (LinearLayout)itemView;
            textName = itemView.findViewById(R.id.textName);
            textMsg = itemView.findViewById(R.id.textMsg);
            textTime = itemView.findViewById(R.id.textTime);
        }
        public void setChat(Chat chat) {
            this.chat = chat;
            textName.setText(chat.user_id);
            textMsg.setText(chat.msg);
            textTime.setText(chat.timestamp+"");
        }
        public void setLayout(){
            if(user_id.equals(chat.user_id)){
                layout.setGravity(Gravity.RIGHT);
            }else{
                layout.setGravity(Gravity.LEFT);
            }
        }
    }
}
