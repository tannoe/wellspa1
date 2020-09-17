package com.example.wellspa.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellspa.model.contact;
import com.example.wellspa.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class contactAdmin extends Fragment {

    private RecyclerView contactList;
    private DatabaseReference contactRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.activity_contact_admin,container, false);

        contactRef = FirebaseDatabase.getInstance().getReference().child("customerMsg");

        contactList = view.findViewById(R.id.recycleContact);
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<contact> options =
                new FirebaseRecyclerOptions.Builder<contact>()
                        .setQuery(contactRef, contact.class)
                        .build();


        FirebaseRecyclerAdapter<contact,contactListViewHolder> adapter =
                new FirebaseRecyclerAdapter<contact, contactListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull contactListViewHolder holder, final int position, @NonNull contact model) {

                        holder.email.setText("Email = "+model.getEmail());
                        holder.firstName.setText("First Name = "+model.getFirst_name());
                        holder.lastName.setText("Last Name = "+model.getLast_name());
                        holder.Messages.setText("Messages = "+model.getMessages());


                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]{

                                        "YES",
                                        "NO"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("ARE YOU SURE TO DELETE THIS Messages ?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        if (i == 0 ){
                                            String uId = getRef(position).getKey();

                                            RemoveOrder(uId);
                                        }
                                        else {

                                        }
                                    }
                                });
                                builder.show();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public contactListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_layout, parent,false);
                        return new contactListViewHolder(view);
                    }
                };
        contactList.setAdapter(adapter);
        adapter.startListening();
    }



    private void RemoveOrder(String uId) {
        contactRef.child(uId).removeValue();
    }


    public static class contactListViewHolder extends RecyclerView.ViewHolder {
        public TextView email,firstName,lastName,Messages;
        public Button deleteBtn;


        public contactListViewHolder(@NonNull View itemView) {
            super(itemView);

            email = itemView.findViewById(R.id.contactEmail);
            firstName = itemView.findViewById(R.id.contactFN);
            lastName = itemView.findViewById(R.id.contactLN);
            Messages = itemView.findViewById(R.id.contactMSG);
            deleteBtn = itemView.findViewById(R.id.deleteBtnContact);


        }
    }

}

