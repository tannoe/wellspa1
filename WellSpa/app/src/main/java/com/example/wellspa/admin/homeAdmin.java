 package com.example.wellspa.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wellspa.drawer;
import com.example.wellspa.login.Main3Activity;
import com.example.wellspa.login.register;
import com.example.wellspa.model.service;
import com.example.wellspa.R;
import com.example.wellspa.data_appointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

 public class homeAdmin extends Fragment {

     private Button addServiceBtn;
     private EditText Inputkode, Inputname, InputPrice;
     String kode,nama,price;
     FirebaseDatabase database = FirebaseDatabase.getInstance();
     DatabaseReference ref;
     service data = new service();
     String key ;

     private ProgressDialog loadingBar;
     @Nullable
     @Override
     public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         View view = null;
         view = inflater.inflate(R.layout.activity_home_admin,container, false);


         addServiceBtn = view.findViewById(R.id.addServicebtn);
         Inputkode = view.findViewById(R.id.kode);
         Inputname = view.findViewById(R.id.name);
         InputPrice = view.findViewById(R.id.price);
         loadingBar = new ProgressDialog(getActivity());

         addServiceBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 kode = Inputkode.getText().toString();
                 nama = Inputname.getText().toString();
                 price = InputPrice.getText().toString();

                 ref = database.getReference();

                 if (TextUtils.isEmpty(nama))
                 {
                     Toast.makeText(getActivity(), "please write your name",Toast.LENGTH_SHORT).show();
                 }
                 else if (TextUtils.isEmpty(kode))
                 {
                     Toast.makeText(getActivity(),"Please write your kode",Toast.LENGTH_SHORT).show();
                 }
                 else if (TextUtils.isEmpty(price))
                 {
                     Toast.makeText(getActivity(), "please write your price",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     loadingBar.setTitle("Create Service");
                     loadingBar.setMessage("Please Wait");
                     loadingBar.setCanceledOnTouchOutside(false);
                     loadingBar.show();

                     ValidateService(nama,kode,price);

                 }
             }
         });


         return view;
    }


     private void ValidateService(final String nama, final String kode, final String price){

         final DatabaseReference Ref;
         Ref = FirebaseDatabase.getInstance().getReference();

         Ref.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (!(dataSnapshot.child("service").child(kode).exists())){
                     HashMap<String, Object> userdatamap = new HashMap<>();
                     userdatamap.put("kode", kode);
                     userdatamap.put("nama", nama);
                     userdatamap.put("price", price);

                     key = Ref.push().getKey();

                     Ref.child("service").child(key).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             if (task.isSuccessful()){
                                 Toast.makeText(getActivity(),"Service successfully created",Toast.LENGTH_SHORT).show();
                                 loadingBar.dismiss();

                                 Intent intent = new Intent(getActivity(), drawer.class);
                                 startActivity(intent);
                             }
                             else {
                                 Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
                 else {
                     Toast.makeText(getActivity(),"This "+ kode + "already exist.",Toast.LENGTH_SHORT).show();
                     loadingBar.dismiss();
                     Toast.makeText(getActivity(),"please try again",Toast.LENGTH_SHORT).show();
                     Intent intent = new Intent(getActivity(), drawer.class);
                     startActivity(intent);
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

     }


}
