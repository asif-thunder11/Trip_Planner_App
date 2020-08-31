package org.thundercorp.tripplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.thundercorp.tripplanner.DataModels.Expense;
import org.thundercorp.tripplanner.DataModels.ExpenseItem;
import org.thundercorp.tripplanner.DataModels.Traveller;
import org.thundercorp.tripplanner.DataModels.TripItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.thundercorp.tripplanner.LoginActivity.CREDS_PREF;
import static org.thundercorp.tripplanner.LoginActivity.CREDS_PREF_KEY_ID;

public class TripsListActivity extends AppCompatActivity implements MyRecyclerAdapter.RecylerViewClickListener{

    private static final String TAG = "[TripsListActivity]->";

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MyRecyclerAdapter myRecyclerAdapter;
    AlertDialog.Builder infoDialogBuilder;
    Toast infoToast;

    ArrayList<TripItem> tripItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        recyclerView = findViewById(R.id.recyler_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        infoDialogBuilder = new AlertDialog.Builder(this);
        infoToast = Toast.makeText(TripsListActivity.this, "", Toast.LENGTH_SHORT);

        myRecyclerAdapter = new MyRecyclerAdapter(tripItems);
        recyclerView.setAdapter(myRecyclerAdapter);
        myRecyclerAdapter.notifyDataSetChanged();
        myRecyclerAdapter.setRecyclerViewClickListener(TripsListActivity.this);

        getUserTrips();

    }

    void getUserTrips(){

        SharedPreferences sp = getSharedPreferences(CREDS_PREF, Context.MODE_PRIVATE);
        String user_id = sp.getString(CREDS_PREF_KEY_ID, "0");
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("user_id", user_id);

        Call<List<TripItem>> callGetUserTrips = Utils.retrofitInterface.executeGetUserTrips(map);
        callGetUserTrips.enqueue(new Callback<List<TripItem>>() {
            @Override
            public void onResponse(Call<List<TripItem>> call, Response<List<TripItem>> response) {
                Log.d(TAG, "onResponse: Code: "+response.code());

                if(response.isSuccessful() && response.body().size()>0){
                    tripItems.clear();
                    tripItems.addAll(response.body());
                    myRecyclerAdapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(TripsListActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<TripItem>> call, Throwable t) {
                Toast.makeText(TripsListActivity.this, "Cannot connect to server!!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showListDialog(String title, ArrayList<String> list){

        View v = getLayoutInflater().inflate(R.layout.dialog_row, null);
        ListView l = v.findViewById(R.id.list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(TripsListActivity.this, android.R.layout.simple_list_item_1, list);
        l.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        infoDialogBuilder.setView(v);
        infoDialogBuilder.setTitle(title);
        infoDialogBuilder.setNeutralButton("Ok", null);
        infoDialogBuilder.create().show();

    }

    @Override
    public void showTravellers(int pos) {
        Log.d(TAG, "showTravellers: CLicked show travellers");

        showListDialog("Travellers", tripItems.get(pos).getTravellers());
    }

    @Override
    public void showExpenses(int pos) {
        Log.d(TAG, "showExpenses: clicked show expenses");

        ArrayList<String> expenseList = new ArrayList<>();
        for(ExpenseItem e: tripItems.get(pos).getExpenses()){

            String s = String.format("%-13s: %s", "Description",e.getName()) ;
            s += String.format("\n%-14s: %s", "Category",e.getCategory()) ;
            s += String.format("\n%-14s: %s Rs", "Amount", e.getAmount());
            s += String.format("\n%-14s: %s\n", "Spent By", e.getUser());

            expenseList.add(s);
        }

        showListDialog("Expenses", expenseList);

    }

    @Override
    public void addTraveller(int pos) {
        Log.d(TAG, "addTraveller: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(TripsListActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_traveller, null);
        builder.setView(v);
        builder.setTitle("Add Traveller");

        builder.setPositiveButton("Add", (dialog, which) -> {
            EditText edtTid = v.findViewById(R.id.editText_id);
            Traveller tr = new Traveller();
            int tid;
            try{
                tid = Integer.parseInt( edtTid.getText().toString() );
            } catch (Exception e){
                infoToast.setText("Invalid ID");
                infoToast.show();
                return;
            }

            tr.setUser_id( tid );
            tr.setTrip_id( tripItems.get(pos).getTrip_id());

            Call<Traveller> c = Utils.retrofitInterface.executeAddTraveller(tr);
            c.enqueue(new Callback<Traveller>() {
                @Override
                public void onResponse(Call<Traveller> call, Response<Traveller> response) {
                    Log.d(TAG, "onResponse: Response Code: "+ response.code());
                    if(response.code()==200){
                        infoToast.setText("Traveller Added\nID: "+tr.getUser_id());
                        infoToast.show();
                        getUserTrips();//reload
                    } else{
                        infoToast.setText("Bad Request\nVerify Data");
                        infoToast.show();
                    }

                }

                @Override
                public void onFailure(Call<Traveller> call1, Throwable t) {
                    Log.d(TAG, "onResponse: Response Code: "+t.getMessage());
                    infoToast.setText("Operation Failed");
                    infoToast.show();
                }
            });
        })
        .setNeutralButton("Cancel", null)
        .show();
    }

    @Override
    public void addExpense(int pos) {

        Log.d(TAG, "addExpense: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(TripsListActivity.this);
        View v = getLayoutInflater().inflate(R.layout.dialog_add_expense, null);
        builder.setView(v);
        builder.setTitle("Add Expense");

        builder.setPositiveButton("Add", ((dialog, which) -> {

            EditText edtUid = v.findViewById(R.id.editText_user_id);
            EditText edtName, edtCat, edtAmt;
            edtName = v.findViewById(R.id.editText_name);
            edtCat = v.findViewById(R.id.editText_category);
            edtAmt = v.findViewById(R.id.editText_amount);

            int uid, amount;
            try{
                uid = Integer.parseInt( edtUid.getText().toString() );
                amount = Integer.parseInt( edtAmt.getText().toString() );
            } catch (Exception e){
                Log.d(TAG, "addExpense: Exception: "+e.toString());
                infoToast.setText("Invalid Data");
                infoToast.show();
                return;
            }

            Expense expense = new Expense();
            expense.setUser_id(uid);
            expense.setTrip_id( tripItems.get(pos).getTrip_id());
            expense.setName( edtName.getText().toString() );
            expense.setCategory( edtCat.getText().toString() );
            expense.setAmount( amount );

            Call<Expense> c = Utils.retrofitInterface.executeAddExpense(expense);
            c.enqueue(new Callback<Expense>() {
                @Override
                public void onResponse(Call<Expense> call, Response<Expense> response) {
                    Log.d(TAG, "onResponse: Response Code: "+ response.code());
                    if(response.code()==200){
                        infoToast.setText("Expense Added: "+expense.getName());
                        infoToast.show();

                        getUserTrips();//reload
                    } else{
                        infoToast.setText("Bad Request\nVerify Data");
                        infoToast.show();
                    }
                }

                @Override
                public void onFailure(Call<Expense> call, Throwable t) {
                    Log.d(TAG, "onResponse: Response Code: "+t.getMessage());
                    infoToast.setText("Operation Failed");
                    infoToast.show();
                }
            });

        }))
        .setNeutralButton("Cancel", null)
        .show();

    }
}