package org.thundercorp.tripplanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.thundercorp.tripplanner.DataModels.User;

import java.util.HashMap;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "[LoginActivity]=>";

    public static final String CREDS_PREF = "CREDENTIALS";
    public static final String CREDS_PREF_KEY_ID = "id";
    public static final String CREDS_PREF_KEY_EMAIL="email";
    public static final String CREDS_PREF_KEY_PW ="pw";
    public static final String CRED_PREF_KEY_INIT="initialized";

    boolean isLoggedIn = false; //to prevent auto opening trip activity; allow coming back to login



    EditText edtTxtEmail, edtTxtPassword;
    Button btnLogin;
    Snackbar infoSnackBar;
    Toast infoToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtTxtEmail = findViewById(R.id.editText_email);
        edtTxtPassword = findViewById(R.id.editText_pw);
        btnLogin = findViewById(R.id.button_login);



        infoSnackBar = Snackbar.make((View)(btnLogin), "", Snackbar.LENGTH_SHORT);
        infoToast = Toast.makeText(LoginActivity.this, "", Toast.LENGTH_LONG);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtTxtEmail.getText().toString();
                String pw = edtTxtPassword.getText().toString();

                if(email.length() < 5 || pw.length()<4){
                    infoSnackBar.setText("Invalid credentials");
                    infoSnackBar.show();
                }
                else{

                    Utils.showProgressDialog(LoginActivity.this, "Logging in..");
                    login(email, pw);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        //auto login if previously logged in
        autoLogin();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void autoLogin(){

        if(isLoggedIn)
            return;

        Utils.showProgressDialog(LoginActivity.this, "Logging In..");

        SharedPreferences sharedPreferences = getSharedPreferences(CREDS_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if( !sharedPreferences.contains(CRED_PREF_KEY_INIT)){
            Log.d(TAG, "autoLogin: Initializing SharedPref");
            editor.putBoolean(CRED_PREF_KEY_INIT, true).commit();
        }
        String email = sharedPreferences.getString(CREDS_PREF_KEY_EMAIL, null);
        String pw = sharedPreferences.getString(CREDS_PREF_KEY_PW, null);

        if( !( email == null || pw == null ) ){
            login(email, pw);
        } else{
            Utils.hideProgressDialog();
        }
    }

    private void login(String email, String pw){
        HashMap<String, String> creds = new HashMap<>();
        creds.put("email", email);
        creds.put("password", pw);

        Call<User> call = Utils.retrofitInterface.executeLogin(creds);
        Log.d(TAG, "login: Connecting to Server");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                Utils.hideProgressDialog();

                Log.d(TAG, "onResponse: Code: "+response.code());
                Log.d(TAG, "onResponse: Body: "+response.body());
                if(response.code()==200){

                    User u = response.body();
                    if(u.getName()==null){
                        infoSnackBar.setText("Email or password is wrong");
                        infoSnackBar.show();
                    } else{
                        //saving to shared pref
                        Utils.saveToSharedPref(getApplicationContext(), CREDS_PREF, CREDS_PREF_KEY_EMAIL, email);
                        Utils.saveToSharedPref(getApplicationContext(), CREDS_PREF, CREDS_PREF_KEY_PW, pw);
                        Utils.saveToSharedPref(getApplicationContext(), CREDS_PREF, CREDS_PREF_KEY_ID, String.valueOf(u.getId()));
                        isLoggedIn = true;
                        infoToast.setText("Logged in As "+u.getName());
                        infoToast.show();

                        startActivity(new Intent(getApplicationContext(), FunctionSelectionActivity.class));
                    }

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Utils.hideProgressDialog();

                infoSnackBar.setText("Operation Failed! Can't connect to server");
                infoSnackBar.show();
            }
        });
    }
}