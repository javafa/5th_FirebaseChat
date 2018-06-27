package com.kodonho.android.firebasechat.domain;

public class Room {
    public String id;
    public String name;
    public int limit;

    public Room(){
        // 빈 생성자 필요 -> 파이어베이스 데이터베이스용
    }
    public Room(String id, String name, int limit){
        this.id = id;
        this.name = name;
        this.limit = limit;
    }
}
