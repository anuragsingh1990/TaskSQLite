package com.peehu.tasksqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.peehu.tasksqlite.Adapter.Adapter_User;
import com.peehu.tasksqlite.Model.User_Model;

import java.util.ArrayList;

public class Dash extends AppCompatActivity {
    RecyclerView rv;
    ArrayList<User_Model> arrayList;
    DBHelper dbHelper;
    TextView logout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dash);
        rv = findViewById(R.id.rv);
        rv.hasFixedSize();
        session = new SessionManager(getApplicationContext());
        rv.setLayoutManager(new LinearLayoutManager(Dash.this));
        dbHelper = new DBHelper(this);
        arrayList = dbHelper.getAllUserList();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                finish();
            }
        });

        if (arrayList.isEmpty()) {
            Toast.makeText(this, "No Data Available", Toast.LENGTH_LONG).show();
        } else {
            Adapter_User adapter_user = new Adapter_User(this, arrayList);
            rv.setAdapter(adapter_user);
        }
    }
}