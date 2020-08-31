package org.thundercorp.tripplanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//for simplicity
public class Utils {
    public static String TAG = "[Utils]->";

    public static ProgressDialog mProgressDialog;
    public static Retrofit retrofit;
    public static RetrofitInterface retrofitInterface;

    static {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofitInterface = retrofit.create(RetrofitInterface.class);
    }

    public static void saveToSharedPref(Context ctx, String sharedPref, String key, String value){
        SharedPreferences.Editor editor = ctx.getSharedPreferences(sharedPref, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();
        Log.d(TAG, "saveToSharedPref: saved-(k, v): ("+key+", "+value+")");
    }


    public static void showProgressDialog(Context context, String msg){
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(msg);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.show();
    }

    public static void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}
