package com.peehu.tasksqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.peehu.tasksqlite.Adapter.Adapter_task;
import com.peehu.tasksqlite.Model.Task_Model;

import java.util.ArrayList;
import java.util.HashMap;

public class DashBoard extends AppCompatActivity {
    TextView logout, users, notask;
    SessionManager session;
    FloatingActionButton floatingActionButton;
    ViewGroup viewGroup;
    DBHelper dbHelper;
    RecyclerView rv_task;
    ArrayList<Task_Model> task_list;
    int currid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_dash_board);

        session = new SessionManager(getApplicationContext());
        task_list = new ArrayList<>();

        rv_task = findViewById(R.id.recyclerView);
        notask = findViewById(R.id.notask);
        rv_task.hasFixedSize();
        rv_task.setLayoutManager(new LinearLayoutManager(DashBoard.this));


        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        dbHelper = new DBHelper(this);
        task_list = dbHelper.getUsersData();
        getAllTask();


        // email
        String name = user.get(SessionManager.KEY_EMAIL);
        logout = findViewById(R.id.logout);
        users = findViewById(R.id.users);
        users.setText(name);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logoutUser();
                finish();
            }
        });
        floatingActionButton = findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllTask();
    }

    private void getAllTask() {
        if (task_list.isEmpty()) {
            notask.setVisibility(View.VISIBLE);
            Adapter_task adapter_task = new Adapter_task(DashBoard.this, task_list);
            rv_task.setAdapter(adapter_task);
        } else {
            for (int i = 0; i < task_list.size(); i++) {
                Adapter_task adapter_task = new Adapter_task(DashBoard.this, task_list);
                rv_task.setAdapter(adapter_task);

            }

        }
    }

    private void OpenDialog() {
        final View dialogView = LayoutInflater.from(DashBoard.this).inflate(R.layout.add_task, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoard.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        ImageView cancel_button = dialogView.findViewById(R.id.cancel_button);
        final EditText ed_taks = dialogView.findViewById(R.id.ed_taks);
        final EditText ed_des = dialogView.findViewById(R.id.ed_description);
        Button sub = dialogView.findViewById(R.id.create_task);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String task = ed_taks.getText().toString().trim();
                String des = ed_des.getText().toString().trim();
                if (task.equalsIgnoreCase("") || des.equalsIgnoreCase("")) {
                    Toast.makeText(DashBoard.this, "Please enter the values", Toast.LENGTH_LONG).show();
                } else {
                    Boolean insert = dbHelper.insertDataUser(task, des);
                    if (insert == true) {
                        Toast.makeText(DashBoard.this, "Task Added Successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(DashBoard.this, DashBoard.class));
                    } else {
                        Toast.makeText(DashBoard.this, "Adding  Failed", Toast.LENGTH_LONG).show();
                    }
                }
            }

        });


    }
}