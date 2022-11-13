package com.example.ComputerScience;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link reportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class reportFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PERMISSION_REQUEST_CODE = 200;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public reportFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment reportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static reportFragment newInstance(String param1, String param2) {
        reportFragment fragment = new reportFragment();
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
        View v = inflater.inflate(R.layout.fragment_report, container, false);

        LocalDateTime dateCurrent = LocalDateTime.now(); // getting and setting current date
        Month currentMonth = dateCurrent.getMonth(); // getting current month
        String currentMonthString = currentMonth.toString(); // getting current month String
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(currentMonthString).child("stats");

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // creating data format
        String currentDateString = dtf.format(dateCurrent); // turns current date into string
        TextView currentDateView = v.findViewById(R.id.statsCurrentDate); // getting current date view
        currentDateView.setText(currentDateString); // setting current date into its view
        TextView currentMonthView = v.findViewById(R.id.statsCurrentMonth); // getting current month view
        currentMonthView.setText("" + currentMonth); // setting current month into its view

        TextView timeSpent = v.findViewById(R.id.totalTimeSpentStat);
        TextView totalCars = v.findViewById(R.id.totalNumberOfCars);
        TextView averageEntrance = v.findViewById(R.id.averageDateOfEntry);
        TextView totalEarnings = v.findViewById(R.id.totalEarnings);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timeSpentData = snapshot.child("averageTimeSpent").getValue().toString();
                timeSpent.setText(timeSpentData + " Hours");

                String totalCarsData = snapshot.child("numberOfCars").getValue().toString();
                totalCars.setText(totalCarsData + " Cars");

                String averageEntranceData = snapshot.child("averageEntranceTime").getValue().toString();
                averageEntrance.setText(averageEntranceData + " Peak");

                String totalEarningsData = snapshot.child("totalEarnings").getValue().toString();
                totalEarnings.setText(totalEarningsData + " GEL");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return v;
    }
}