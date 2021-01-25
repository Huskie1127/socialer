package com.kuaishou.socialer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class informationListActivity extends AppCompatActivity {
    static List<collectedUser> collectedUsers = new ArrayList<collectedUser>();
    static informationAdapter informationAdapter = new informationAdapter(collectedUsers);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_list);
        collectedUser collectedUser = new collectedUser("默认姓名","默认手机号","默认qq","默认微信","默认微博");
        collectedUsers.add(collectedUser);
        RecyclerView recyclerView = findViewById(R.id.informationrecyclerView);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(informationAdapter);
    }
}