package com.example.wellspa.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wellspa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {

        private Button CreateAccountButton;
        private EditText InputName, InputPhoneNumber, InputPassword;
        private ProgressDialog loadingBar;
        @Override
        protected void onCreate ( final Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountButton = (Button) findViewById(R.id.registerBtn);
        InputName = (EditText) findViewById(R.id.usernameReg);
        InputPassword = (EditText) findViewById(R.id.passwordReg);
        InputPhoneNumber = (EditText) findViewById(R.id.phoneReg);
        loadingBar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
        }

        private void CreateAccount(){
            String name = InputName.getText().toString();
            String phone = InputPhoneNumber.getText().toString();
            String password = InputPassword.getText().toString();


            if (TextUtils.isEmpty(name))
            {
                Toast.makeText(this, "please write your name",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(phone))
            {
                Toast.makeText(this, "please write your phone",Toast.LENGTH_SHORT).show();
            }
            else if (TextUtils.isEmpty(password))
            {
                Toast.makeText(this, "please write your password",Toast.LENGTH_SHORT).show();
            }
            else {
                loadingBar.setTitle("Create Account");
                loadingBar.setMessage("Please Wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidateName(name,phone,password);

            }
        }

        private void ValidateName(final String name, final String phone, final String password){

            final DatabaseReference RootRef;
            RootRef = FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.child("Users").child(name).exists())){
                        HashMap<String, Object> userdatamap = new HashMap<>();
                        userdatamap.put("name", name);
                        userdatamap.put("phone", phone);
                        userdatamap.put("password", password);

                        RootRef.child("Users").child(name).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(register.this,"Your account has successfully created",Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(register.this, Main3Activity.class);
                                    startActivity(intent);
                                }
                                else {
                                    Toast.makeText(register.this,"Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(register.this,"This "+ name + "already exist.",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Toast.makeText(register.this,"please try again",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(register.this, Main3Activity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }





