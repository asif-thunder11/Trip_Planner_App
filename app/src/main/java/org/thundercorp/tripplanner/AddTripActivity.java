package org.thundercorp.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.thundercorp.tripplanner.DataModels.TripUpload;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddTripActivity extends AppCompatActivity {
    private static final String TAG = "[AddTripActivity]->";

    Toast toast;
    Snackbar snackbar;

    Button btnCreateTrip;
    EditText edtName, edtPackage, edtRoute, edtStartDate, edtEndDate, edtTravellerIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        edtName = findViewById(R.id.editText_name);
        edtPackage = findViewById(R.id.editText_trip_package);
        edtRoute = findViewById(R.id.editText_route);
        edtStartDate = findViewById(R.id.editText_start_date);
        edtEndDate = findViewById(R.id.editText_end_date);
        edtTravellerIDs = findViewById(R.id.editText_travellers_id);
        btnCreateTrip = findViewById(R.id.button_create_trip);

        toast = Toast.makeText(AddTripActivity.this, "", Toast.LENGTH_LONG);
        snackbar = Snackbar.make( (View) (btnCreateTrip), "",Snackbar.LENGTH_SHORT);
        btnCreateTrip.setOnClickListener(v -> {

            TripUpload t = new TripUpload();
            t.setName( edtName.getText().toString() );
            t.setTrip_package_id( Integer.parseInt(edtPackage.getText().toString()) );
            t.setRoute_id( Integer.parseInt( edtRoute.getText().toString() ));
            t.setStart_date( edtStartDate.getText().toString() );
            t.setEnd_date( edtEndDate.getText().toString() );

            SharedPreferences sp = getSharedPreferences(LoginActivity.CREDS_PREF, Context.MODE_PRIVATE);
            t.setCreator_id( Integer.parseInt(sp.getString(LoginActivity.CREDS_PREF_KEY_ID, "0") ) );

            ArrayList<String> tr = new ArrayList<>();

            String travellers = edtTravellerIDs.getText().toString();
            for(String s: travellers.split(",")){
                if(s.trim().length()>0)
                    tr.add(s.trim());
            }
            Log.d(TAG, "onCreate: travellers: "+ tr.toString());
            t.setTraveller_ids(tr);

            Call<TripUpload> call = Utils.retrofitInterface.executePutTrip(t);
            call.enqueue(new Callback<TripUpload>() {
                @Override
                public void onResponse(Call<TripUpload> call, Response<TripUpload> response) {

                    Log.d(TAG, "onResponse: Code: "+response.code());

                    if(response.code()==200){
                        snackbar.setText("Added Successfully");
                        snackbar.show();
                    }
                    else {
                        toast.setText("Bad Request\nResponseCode: "+response.code()+"\nMessage: "+response.body());
                        toast.show();
                    }
                }
                @Override
                public void onFailure(Call<TripUpload> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    snackbar.setText("Failed");
                    snackbar.show();
                }
            });

        });
    }
}