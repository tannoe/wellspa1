package com.example.wellspa.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wellspa.Prevalent.Prevalent;
import com.example.wellspa.R;
import com.example.wellspa.login.Main3Activity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class account extends Fragment {

    GoogleSignInClient mGoogleSignInClient;
    Button logoutBtn;
    TextView nameTV, emailTV, idTV;
    ImageView photoIV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = null;
        view = inflater.inflate(R.layout.fragment_account,container, false);

        logoutBtn = view.findViewById(R.id.logout);
        nameTV = view.findViewById(R.id.name);
        emailTV = view.findViewById(R.id.email);
        idTV = view.findViewById(R.id.id);
        photoIV = view.findViewById(R.id.photo);

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

            nameTV.setText(personName);
            emailTV.setText("Email: "+personEmail);
            idTV.setText("Id: "+personId);
            Glide.with(this).load(personPhoto).into(photoIV);

        } else {
            nameTV.setText(Prevalent.CurrentOnlineUser.getName());
            emailTV.setText(Prevalent.CurrentOnlineUser.getPhone());
        }
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        return view;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Sign Out Success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), Main3Activity.class));

                    }
                });
    }

}
