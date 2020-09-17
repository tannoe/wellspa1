package com.example.wellspa.admin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellspa.R;
import com.example.wellspa.model.service;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class tblService extends Fragment {

    private RecyclerView serviceList;
    private DatabaseReference serviceRef;
    private EditText editKode, editNama, editPrice ;

    String key;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.activity_table_service,container, false);

        serviceRef = FirebaseDatabase.getInstance().getReference().child("service");

        serviceList = view.findViewById(R.id.recycleService);
        serviceList.setLayoutManager(new LinearLayoutManager(getActivity()));



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<service> options =
                new FirebaseRecyclerOptions.Builder<service>()
                        .setQuery(serviceRef, service.class)
                        .build();

        FirebaseRecyclerAdapter<service, tableService.serviceListViewHolder> adapter =
                new FirebaseRecyclerAdapter<service, tableService.serviceListViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull tableService.serviceListViewHolder holder, final int position, @NonNull final service model) {

                        holder.kode.setText("code = "+model.getKode());
                        holder.name.setText("Name =" +model.getNama());
                        holder.price.setText("Price = "+ model.getPrice()+ "$");
                        holder.UpdateBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                key = getRef(position).getKey();
                                dialogUpdateService(model);

                            }
                        });
                        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options[] = new CharSequence[]{

                                        "YES",
                                        "NO"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle("ARE YOU SURE TO DELETE THIS SERVICE ?");

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
                    public tableService.serviceListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_layout, parent,false);
                        return new tableService.serviceListViewHolder(view);
                    }
                };
        serviceList.setAdapter(adapter);
        adapter.startListening();
    }



    public static class serviceListViewHolder extends RecyclerView.ViewHolder {
        public TextView name,kode,price;
        public Button deleteBtn,UpdateBtn;


        public serviceListViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.serviceName);
            kode = itemView.findViewById(R.id.serviceCode);
            price = itemView.findViewById(R.id.servicePrice);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
            UpdateBtn = itemView.findViewById(R.id.updateBtn);

        }
    }


    private void RemoveOrder(String uId) {
        serviceRef.child(uId).removeValue();
    }

    private void dialogUpdateService(final service service){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update Service");
        View view = getLayoutInflater().inflate(R.layout.update_service, null);

        editKode = view.findViewById(R.id.kodeService);
        editNama = view.findViewById(R.id.namaService);
        editPrice = view.findViewById(R.id.priceService);


        editKode.setText(service.getKode());
        editNama.setText(service.getNama());
        editPrice.setText(service.getPrice());
        builder.setView(view);

        if (service != null){
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    service.setKode(editKode.getText().toString());
                    service.setNama(editNama.getText().toString());
                    service.setPrice(editPrice.getText().toString());
                    updateDataService(service);
                }
            });
        }
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void updateDataService(service service) {

        serviceRef.child(key).setValue(service).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(), "Update Succes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
