package com.example.wellspa.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellspa.Main2Activity;
import com.example.wellspa.Prevalent.Prevalent;
import com.example.wellspa.drawer;
import com.example.wellspa.R;
import com.example.wellspa.model.Users;
import com.example.wellspa.model.admin;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    private EditText InputName, InputPassword;
    private Button LoginBtn;
    private ProgressDialog loadingBar;
    private TextView AdminLink, NotAdminLink;
    private String parentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LoginBtn = (Button) findViewById(R.id.loginBtn1);
        InputName = (EditText) findViewById(R.id.usernameLog);
        InputPassword = (EditText) findViewById(R.id.passwordLog);
        AdminLink = (TextView) findViewById(R.id.adminLog);
        NotAdminLink = (TextView) findViewById(R.id.UserLog);
        loadingBar = new ProgressDialog(this);

        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

        AdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBtn.setText("Login Admin");
                AdminLink.setVisibility(View.INVISIBLE);
                NotAdminLink.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        NotAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginBtn.setText("Login");
                AdminLink.setVisibility(View.VISIBLE);
                NotAdminLink.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }
    private void LoginUser(){

        String name = InputName.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(this, "please write your name",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "please write your password",Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(name, password);

        }
    }

    private void AllowAccessToAccount(final String name, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(name).exists()){

                    admin admindata = dataSnapshot.child(parentDbName).child(name).getValue(admin.class);
                    Users userdata = dataSnapshot.child(parentDbName).child(name).getValue(Users.class);
                    if (userdata.getName().equals(name)){
                        if (userdata.getPassword().equals(password)) {
                                    if (parentDbName.equals("Admins")) {

                                        Toast.makeText(Login.this, "Login Succes", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Login.this, drawer.class);
                                        Prevalent.CurrentOnlineAdmin = admindata;
                                        startActivity(intent);
                                        finish();
                                        Main3Activity.fa.finish();
                                    } else if (parentDbName.equals("Users")) {
                                        Toast.makeText(Login.this, "Login Succes", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(Login.this, Main2Activity.class);
                                        Prevalent.CurrentOnlineUser = userdata;
                                        startActivity(intent);
                                        finish();
                                        Main3Activity.fa.finish();
                                    }
                                }
                            }
                        }

                else {
                    Toast.makeText(Login.this,"Account with this" + name + "do not exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
