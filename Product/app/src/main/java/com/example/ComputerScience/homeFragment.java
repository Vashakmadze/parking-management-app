package com.example.ComputerScience;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.Month;


public class homeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView rvVehicles;
    private DatabaseReference carsRef;

    public homeFragment() {
    }

    public static homeFragment newInstance(String param1, String param2) {
        homeFragment fragment = new homeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View carsView = inflater.inflate(R.layout.fragment_home, container, false);

        rvVehicles = carsView.findViewById(R.id.activeRecyclerView); // getting recycler view
        rvVehicles.setLayoutManager(new LinearLayoutManager(carsView.getContext())); // initialize recycler view
        LocalDateTime dateCurrent = LocalDateTime.now(); // getting and setting current date
        Month currentMonth = dateCurrent.getMonth(); // getting current month
        String currentMonthString = currentMonth.toString(); // getting current month String
        carsRef = FirebaseDatabase.getInstance().getReference().child(currentMonthString).child("cars"); // getting database reference

        return carsView;
    }

    @Override
    public void onStart() { // on start method of fragment
        super.onStart();
        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<car>() // crearing firebase ui option
                .setQuery(carsRef, car.class).build(); // referencing database and car class in option of firebase ui

        FirebaseRecyclerAdapter<car, carViewHolder> adapter = new FirebaseRecyclerAdapter<car, carViewHolder>(options) { // creating firebase recycler view adapter
            @Override
            protected void onBindViewHolder(@NonNull carViewHolder holder, int position, @NonNull car model) {

                 String carId = getRef(position).getKey(); // getting car id from database by getting the key
                 holder.itemView.setOnClickListener(new View.OnClickListener() { // setting on click listener on our item in recycler view
                     @Override
                     public void onClick(View v) {
                         Intent intent = new Intent(getContext(), finishActivity.class); // creating intent
                         intent.putExtra("id", carId); // putting id into intent
                         startActivity(intent); // initializing intent

                     }
                 });

                 carsRef.child(carId).addValueEventListener(new ValueEventListener() { // adding listener to listen for data change in car in database
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         if(snapshot.exists()) {
                             String numberPlateString = snapshot.child("numberPlate").getValue().toString(); // getting numberplate from database
                             String entranceDateString = snapshot.child("entranceDate").getValue().toString(); // getting entrance date from database
                             String color = snapshot.child("color").getValue().toString(); // getting color from database
                             String type = snapshot.child("type").getValue().toString(); // getting type from database


                             holder.numberPlate.setText(numberPlateString); // setting numberplate
                             holder.entranceDate.setText("Entry Time: " + entranceDateString); // setting entrance date
                             holder.type.setText(type); // setting type
                             holder.color.setText(color); // setting color
                         }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


            }

            @NonNull
            @Override
            public carViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_layout, parent, false);
                carViewHolder viewHolder = new carViewHolder(view);

                return viewHolder;

            }
        };

        rvVehicles.setAdapter(adapter); // setting adapter for our recycler view
        adapter.startListening(); // starts listening for database changes and populates adapter

    }


    public static class carViewHolder extends RecyclerView.ViewHolder {

        TextView numberPlate, entranceDate, type, color;

        public carViewHolder(@NonNull View itemView) {
            super(itemView);

            numberPlate = itemView.findViewById(R.id.numberPlate); // getting view of numberplate
            entranceDate = itemView.findViewById(R.id.entranceDate); // getting view of entranceDate
            type = itemView.findViewById(R.id.type); // getting view of type
            color = itemView.findViewById(R.id.color); // getting view of color


        }
    }

}