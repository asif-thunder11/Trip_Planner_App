package org.thundercorp.tripplanner;

import org.thundercorp.tripplanner.DataModels.Expense;
import org.thundercorp.tripplanner.DataModels.Traveller;
import org.thundercorp.tripplanner.DataModels.TripUpload;
import org.thundercorp.tripplanner.DataModels.TripItem;
import org.thundercorp.tripplanner.DataModels.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface RetrofitInterface {


    @POST("/login")
    Call<User> executeLogin(@Body HashMap<String, String> creds);

    @POST("/getUsers")
    Call<ArrayList<User>> executeGetUsers();

    @POST("/getUserTrips")
    Call<List<TripItem>> executeGetUserTrips(@Body HashMap<String, String> map);

    @PUT("/addTrip")
    Call<TripUpload> executePutTrip(@Body TripUpload tripUpload);

    @PUT("/addTraveller")
    Call<Traveller> executeAddTraveller(@Body Traveller traveller);

    @PUT("/addExpense")
    Call<Expense> executeAddExpense(@Body Expense expense);

}
