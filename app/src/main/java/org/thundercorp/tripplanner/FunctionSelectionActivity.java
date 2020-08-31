package org.thundercorp.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FunctionSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "[FunctionSelection]->";
   private Button btnShowTrip, btnAddTrip, btnRunQueries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function_selection);

        btnShowTrip = findViewById(R.id.button_show_trips);
        btnAddTrip = findViewById(R.id.button_create_trip);
        btnRunQueries = findViewById(R.id.button_run_queries);
        btnShowTrip.setOnClickListener(this::onClick);
        btnAddTrip.setOnClickListener(this::onClick);
        btnRunQueries.setOnClickListener(this::onClick);

        //TODO
        //run any query from app
        btnRunQueries.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if( id == btnShowTrip.getId()){

            startActivity( new Intent(getApplicationContext(), TripsListActivity.class));
        }
        else if(id == btnAddTrip.getId()){

            startActivity(new Intent(getApplicationContext(), AddTripActivity.class));
        }
        else {

        }
    }
}