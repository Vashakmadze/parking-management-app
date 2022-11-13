package com.example.ComputerScience;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LocalDateTime dateCurrent;

    public addFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static addFragment newInstance(String param1, String param2) {
        addFragment fragment = new addFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Button button = view.findViewById(R.id.submit_client); // getting button view
        TextView date = view.findViewById(R.id.timeOfEntry); // getting time of entry view
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // creating data format
        dateCurrent = LocalDateTime.now(); // getting and setting current date
        Month currentMonth = dateCurrent.getMonth(); // getting current month
        String currentMonthString = currentMonth.toString(); // getting current month String
        String dateOfEntry = dtf.format(dateCurrent); // formating current date and saving it to string
        date.setText(dateOfEntry);  // setting date of entry to view

        button.setOnClickListener(new View.OnClickListener() { // creating on click listener for button
            @Override
            public void onClick(View v) {

                TextInputLayout numberLayout = view.findViewById(R.id.carNumberInputLayout); // getting numberLayout view
                String number = numberLayout.getEditText().getText().toString(); // getting input from numberLayout
                TextInputLayout carTypeLayout = view.findViewById(R.id.carTypeInputLayout); // getting carTypeLayout view
                String carType = carTypeLayout.getEditText().getText().toString(); // getting input from carTypeLayout
                TextInputLayout colorLayout = view.findViewById(R.id.carColorInputLayout); // getting colorLayout view
                String color = colorLayout.getEditText().getText().toString(); // getting input from colorLayout
                TextView dateView = view.findViewById(R.id.timeOfEntry); // getting dateView


                FirebaseDatabase database = FirebaseDatabase.getInstance(); // getting database instance
                DatabaseReference myRef = database.getReference().child(currentMonthString); // creating database reference for cars

                car carActive = new car(number,dateOfEntry,color,carType); // creating car from car constructor
                myRef.child("cars").push().setValue(carActive);// adding our car to database

                Intent intent = new Intent(v.getContext(), MainActivity.class); // creating intent to get back to main activity
                startActivity(intent); // initializing intent

            }
        });




        return view;
    }
}