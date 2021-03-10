package com.peehu.tasksqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpAct extends AppCompatActivity {
    TextView textView2;
    EditText ed_name, ed_email, ed_password;
    Button bt_signup;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sign_up);

        textView2 = findViewById(R.id.textView2);
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        bt_signup = findViewById(R.id.button_signup);

        dbHelper = new DBHelper(this);


        textView2.setText(R.string.login);
        String text_sign_up = textView2.getText().toString().trim();
        int i1 = text_sign_up.indexOf("L");
        int i2 = text_sign_up.indexOf("n");
        textView2.setMovementMethod(LinkMovementMethod.getInstance());
        textView2.setText(text_sign_up, TextView.BufferType.SPANNABLE);
        Spannable mySpannable = (Spannable) textView2.getText();
        ClickableSpan myClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(SignUpAct.this, MainActivity.class));
            }
        };
        mySpannable.setSpan(myClickableSpan, i1, i2 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ed_name.getText().toString().trim();
                String email = ed_email.getText().toString().trim();
                String password = ed_password.getText().toString().trim();
                if (name.equalsIgnoreCase("") || email.equalsIgnoreCase("") || password.equalsIgnoreCase("")) {
                    Toast.makeText(SignUpAct.this, "Please enter the values", Toast.LENGTH_LONG).show();
                } else {
                    Boolean checkuser = dbHelper.checkUsername(email);
                    if (checkuser == false) {
                        Boolean insert = dbHelper.insertData(name,email, password);
                        if (insert==true){
                            Toast.makeText(SignUpAct.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SignUpAct.this,DashBoard.class));
                        }else {
                            Toast.makeText(SignUpAct.this,"Registration  Failed",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(SignUpAct.this,"User already exists",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}