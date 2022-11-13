package com.example.ComputerScience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation); // getting the id of bottom navigation
        bottomNavigation.setOnItemSelectedListener(navListener); // using our defined listener to listen and act on item clicks

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new homeFragment()).commit();  // setting home fragment as main fragment

    }

    private NavigationBarView.OnItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() { // listener that listens to bottom navigation clicks and using switch chooses the fragment
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new homeFragment();
                    break;

                case R.id.add:
                    selectedFragment = new addFragment();
                    break;

                case R.id.stats:
                    selectedFragment = new reportFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,
                    selectedFragment).commit(); // committing the transaction by changing fragment layout with chosen layout

            return true;

        }
    };

}