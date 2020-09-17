package com.example.wellspa.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wellspa.R;
import com.example.wellspa.model.service;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class home extends Fragment {

    private DatabaseReference ref;
    private RecyclerView userServiceList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);



        ref = FirebaseDatabase.getInstance().getReference().child("service");
        userServiceList = view.findViewById(R.id.userRecycleView);
        userServiceList.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<service> options =
                new FirebaseRecyclerOptions.Builder<service>()
                        .setQuery(ref, service.class)
                        .build();

        FirebaseRecyclerAdapter<service, home.serviceListViewHolder> adapter =
                new FirebaseRecyclerAdapter<service, home.serviceListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull home.serviceListViewHolder holder, final int position, @NonNull service model) {

                        holder.user_name.setText(model.getNama());
                        holder.user_price.setText(model.getPrice()+ "$");


                    }

                    @NonNull
                    @Override
                    public home.serviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_service_layout, parent,false);
                        return new home.serviceListViewHolder(view);
                    }
                };
        userServiceList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class serviceListViewHolder extends RecyclerView.ViewHolder {
        public TextView user_name,user_price;


        public serviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            user_name = itemView.findViewById(R.id.user_service_name);
            user_price = itemView.findViewById(R.id.user_service_price);

        }
    }

}
