package com.example.ComputerScience;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class finishActivity extends AppCompatActivity {

    private TextView priceView;
    private TextView durationView;
    private TextView timeLeftView;
    private TextView carNumberView;
    private View finishButton;
    private View deleteButton;
    private TextView entranceDate;
    private String carId;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        LocalDateTime dateCurrent = LocalDateTime.now(); // getting and setting current date
        Month currentMonth = dateCurrent.getMonth(); // getting current month
        String currentMonthString = currentMonth.toString(); // getting current month String
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child(currentMonthString); // getting cars database reference

        carId = getIntent().getStringExtra("id"); // getting id of the car from last activity

        myRef.addListenerForSingleValueEvent(new ValueEventListener() { // adding listener on database key which is our car
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //delete button
                deleteButton = findViewById(R.id.delete_client);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snapshot.getRef().child("cars").child(carId).removeValue(); // on click removing the car from database
                        Intent intent = new Intent(v.getContext(), MainActivity.class); // creating intent to get back to main activity
                        startActivity(intent); // initializing intent
                    }
                });


                if (snapshot.exists()) {
                    // car number
                    String carNumbermberPlate = snapshot.child("cars").child(carId).child("numberPlate").getValue().toString(); // getting number plate from database
                    carNumberView = findViewById(R.id.carNumberTextView); // getting number plate view
                    carNumberView.setText(carNumbermberPlate); // setting the number plate into a view

                    // entrance date
                    String entranceTime = snapshot.child("cars").child(carId).child("entranceDate").getValue().toString(); // getting entrance date from database
                    entranceDate = findViewById(R.id.timeOfEntryTextView); // getting the view
                    entranceDate.setText("Entrance Date: " + entranceTime); // setting the entrance date into the view

                    // current date
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"); // creating date format
                    LocalDateTime dateCurrent = LocalDateTime.now(); // getting current date and saving it
                    LocalDateTime dateEntered = LocalDateTime.parse(entranceTime, dtf); // getting entered date string and converting into localdatetime with our formated date time
                    timeLeftView = findViewById(R.id.timeOfLeaveTextView); // getting time of leave view
                    timeLeftView.setText("Date of leave: " + dtf.format(dateCurrent)); // setting the result in the view


                    // time spent = current date - entrance date
                    durationView = findViewById(R.id.durationTextView); // getting duration view
                    long hours = ChronoUnit.HOURS.between(dateEntered, dateCurrent); // calculating amount of hours between entrance and current date
                    durationView.setText("Duration (In Hours): " + hours); // setting the result in duration view


                    // cost = hour x 2
                    long cost = hours * 2; // calculating cost by multiplying amount of hours by 2
                    String totalCost = String.valueOf(cost); // converting long to string to display
                    priceView = findViewById(R.id.priceTextView); // getting price view to put result in
                    priceView.setText("GEL: "+ totalCost); // putting result in

                    //finish button
                    finishButton = findViewById(R.id.finishActivityButton); // getting finish button view
                    finishButton.setOnClickListener(new View.OnClickListener() { // adding listener to listeon to clic
                        @Override
                        public void onClick(View v) {

                            // updating earnings
                            if (snapshot.child("stats").exists()) {
                                String databaseEarnings = snapshot.child("stats").child("totalEarnings").getValue().toString(); // getting earnings in string
                                int earnings = Integer.parseInt(totalCost) + Integer.parseInt(databaseEarnings); // updating total earnings while converting values to int
                                snapshot.getRef().child("stats").child("totalEarnings").setValue(earnings); // adding updated value to database

                                // updating time spent
                                String databaseTimeSpent = snapshot.child("stats").child("averageTimeSpent").getValue().toString(); // getting time spet in string
                                int timeSpent = Math.toIntExact(hours) + Integer.parseInt(databaseTimeSpent); // updating total time spent while converting values to int
                                snapshot.getRef().child("stats").child("averageTimeSpent").setValue(timeSpent); // adding updated value to database

                                // updating number of cars entrance
                                String databaseNumberOfCars = snapshot.child("stats").child("numberOfCars").getValue().toString();
                                int numberOfCars = Integer.parseInt(databaseNumberOfCars) + 1;
                                snapshot.getRef().child("stats").child("numberOfCars").setValue(numberOfCars);

                                //updating average time of entrance
                                String databaseLocalTime = snapshot.child("stats").child("averageEntranceTime").getValue().toString();
                                DateTimeFormatter dt = DateTimeFormatter.ofPattern("HH.mm");
                                LocalTime databaseTime = LocalTime.parse(databaseLocalTime, dt);

                                String currentTimeString = LocalDateTime.parse(entranceTime, dtf).toLocalTime().format(dt);
                                LocalTime currentTime = LocalTime.parse(currentTimeString, dt);

                                Duration diff = Duration.between(databaseTime, currentTime);
                                LocalTime midpoint = databaseTime.plus(diff.dividedBy(2));
                                snapshot.getRef().child("stats").child("averageEntranceTime").setValue(midpoint.format(dt));

                            } else {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH.mm");

                                snapshot.getRef().child("stats").child("totalEarnings").setValue(Integer.parseInt(totalCost));
                                snapshot.getRef().child("stats").child("averageTimeSpent").setValue(Math.toIntExact(hours));
                                snapshot.getRef().child("stats").child("numberOfCars").setValue(1);
                                snapshot.getRef().child("stats").child("averageEntranceTime").setValue(LocalDateTime.parse(entranceTime, dtf).toLocalTime().format(formatter));

                            }

                            snapshot.getRef().child("cars").child(carId).removeValue(); // on click removing the car from database
                            Intent intent = new Intent(v.getContext(), MainActivity.class); // creating intent to get back to main activity
                            startActivity(intent); // initializing intent
                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

}
