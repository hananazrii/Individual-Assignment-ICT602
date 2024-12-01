package com.example.individualassignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private TextView displayRebate, displayTotal;
    private EditText numElectricity, rebateAmount;
    private Button calcButton, resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize UI elements
        displayRebate = findViewById(R.id.displayRebate);
        displayTotal = findViewById(R.id.displayTotal);
        numElectricity = findViewById(R.id.numElectricity);
        rebateAmount = findViewById(R.id.rebateAmount);
        calcButton = findViewById(R.id.calcButton);
        resetButton = findViewById(R.id.resetButton);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                } else if (item.getItemId() == R.id.instruction) {
                    startActivity(new Intent(MainActivity.this, InstructionActivity.class));
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Button click listeners
        calcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateCharges();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetInputs();
            }
        });
    }

    private void calculateCharges() {
        String number = numElectricity.getText().toString().trim();
        String rebate = rebateAmount.getText().toString().trim();

        if (number.isEmpty() || rebate.isEmpty()) {
            Toast.makeText(this, "Please enter valid values.", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            int value = Integer.parseInt(number);
            float rebateValue = Float.parseFloat(rebate);

            if (rebateValue < 0 || rebateValue > 5) {
                Toast.makeText(this, "Rebate must be between 0% and 5%.", Toast.LENGTH_LONG).show();
                return;
            }

            float totalCharges;
            if (value <= 200) {
                totalCharges = value * 0.218f;
            } else if (value <= 300) {
                totalCharges = (200 * 0.218f) + ((value - 200) * 0.334f);
            } else if (value <= 600) {
                totalCharges = (200 * 0.218f) + (100 * 0.334f) + ((value - 300) * 0.516f);
            } else {
                totalCharges = (200 * 0.218f) + (100 * 0.334f) + (300 * 0.516f) + ((value - 600) * 0.546f);
            }

            float finalCharge = totalCharges - (totalCharges * (rebateValue / 100));

            displayRebate.setText(String.format("Total Charges: RM %.2f", totalCharges));
            displayTotal.setText(String.format("Your Bill After Rebate: RM %.2f", finalCharge));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input format.", Toast.LENGTH_LONG).show();
        }
    }

    private void resetInputs() {
        numElectricity.setText("");
        rebateAmount.setText("");
        displayRebate.setText("Total Charges:");
        displayTotal.setText("Your Bill After Rebate:");
        Toast.makeText(this, "Inputs cleared.", Toast.LENGTH_SHORT).show();
    }
}
