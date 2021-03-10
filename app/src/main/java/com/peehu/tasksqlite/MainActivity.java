package com.peehu.tasksqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView2;
    EditText ed_username, ed_password;
    Button login;
    DBHelper dbHelper;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;
    SessionManager session;
    String admin = "admin@gmail.com";
    String pass = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        session = new SessionManager(getApplicationContext());


        if (session.isLoggedIn()) {
            AppConstants.name = Method.getPreferences(MainActivity.this, "name");
            Intent intent = new Intent(MainActivity.this, DashBoard.class);
            /*intent.putExtra("name", name);*/
            startActivity(intent);

            finish();
        }

        textView2 = findViewById(R.id.textView2);
        ed_username = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        login = findViewById(R.id.button);
        dbHelper = new DBHelper(MainActivity.this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String usrnm = ed_username.getText().toString();
        final String pas = ed_password.getText().toString();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Dash.class));
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = ed_username.getText().toString().trim();
                String password = ed_password.getText().toString().trim();

                if (username.equalsIgnoreCase(admin) && password.equalsIgnoreCase(pass)) {
                    startActivity(new Intent(MainActivity.this, Dash.class));
                    finish();
                } else {

                    Method.savePreferences(MainActivity.this, "usename", username);
                    if (username.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                        Toast.makeText(MainActivity.this, "Please enter all fields", Toast.LENGTH_LONG).show();
                    } else {
                        Boolean checkuser_pass = dbHelper.checkUsernamePassword(username, password);
                        if (checkuser_pass == true) {
                            Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            String email = Method.getPreferences(MainActivity.this, "usename");
                            session.createLoginSession(email);
                            startActivity(new Intent(MainActivity.this, DashBoard.class));
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                    }
                }


            }
        });


        textView2.setText(R.string.signup);
        String text_sign_up = textView2.getText().toString().trim();
        int i1 = text_sign_up.indexOf("S");
        int i2 = text_sign_up.indexOf("p");
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setText(text_sign_up, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable) textView2.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(MainActivity.this, SignUpAct.class));
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


    }
}