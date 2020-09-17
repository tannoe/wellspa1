package com.example.wellspa.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wellspa.Prevalent.Prevalent;
import com.example.wellspa.R;
import com.example.wellspa.data_appointment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class appointment extends Fragment {

    EditText emailET, firstnameET, lastnameET, messageET;
    Button btnSend;
    DatabaseReference ref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    data_appointment data = new data_appointment();
    String email,firstname, lastname, message;
    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        view = inflater.inflate(R.layout.fragment_appointment,container, false);

        emailET = view.findViewById(R.id.emails);
        firstnameET = view.findViewById(R.id.firstname);
        lastnameET = view.findViewById(R.id.lastname);
        messageET = view.findViewById(R.id.messages);
        btnSend = view.findViewById(R.id.btnSend);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (account != null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();

            emailET.setText(personEmail);
            firstnameET.setText(personGivenName);
            lastnameET.setText(personFamilyName);

        } else {
            firstnameET.setText(Prevalent.CurrentOnlineUser.getName());
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailET.getText().toString();
                firstname = firstnameET.getText().toString();
                lastname = lastnameET.getText().toString();
                message = messageET.getText().toString();

                ref = database.getReference();

                getValue();

                if (emailET.equals("") && firstnameET.equals("") && lastnameET.equals("") && messageET.equals("")){
                    Toast.makeText(getActivity(), "please insert all data", Toast.LENGTH_LONG).show();
                }else {
                    ref.child("customerMsg").push()
                            .setValue(data);
                    Toast.makeText(getActivity(),"Data has been sent", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void getValue(){
        data.setEmail(emailET.getText().toString());
        data.setFirst_name(firstnameET.getText().toString());
        data.setLast_name(lastnameET.getText().toString());
        data.setMessages(messageET.getText().toString());
    }
}
