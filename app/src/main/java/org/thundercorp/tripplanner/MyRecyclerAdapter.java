package org.thundercorp.tripplanner;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.thundercorp.tripplanner.DataModels.TripItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private static final String TAG = "[MyRecyclerAdapter]->";

    private List<TripItem> tripItems;
    private RecylerViewClickListener recylerViewClickListener;



    public MyRecyclerAdapter(List<TripItem> tripItems){
        this.tripItems = tripItems;
        Log.d(TAG, "MyRecyclerAdapter: "+tripItems.size());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Log.d(TAG, "onCreateViewHolder: ");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item_row, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(v, recylerViewClickListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        TripItem currentItem = tripItems.get(position);
        holder.title.setText(currentItem.getTitle());

        String details="Creator: " + currentItem.getCreator();
        details += "\nTrip Package: " + currentItem.getTrip_package();
        details += "\nFrom: " + currentItem.getFrom();
        details += "\nTo: " + currentItem.getTo();

        //DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");

        details += "\nStart Date: " + currentItem.getStart_date();
        details += "\nEnd Date: " + currentItem.getEnd_date();
        details += "\nDuration: " + currentItem.getDuration() + " Months";

        holder.details.setText(details);

    }

    @Override
    public int getItemCount() {
        return tripItems.size();
    }


    public interface RecylerViewClickListener{
        void showTravellers(int pos);
        void showExpenses(int pos);
        void addTraveller(int pos);
        void addExpense(int pos);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private static final String TAG = "[MyViewHolder]->";

        TextView title, details;
        Button travellers, expenses, btnAddTraveller, btnAddExpense;

        public MyViewHolder(@NonNull View itemView, final RecylerViewClickListener recylerViewClickListener) {
            super(itemView);
            Log.d(TAG, "MyViewHolder: ");
            title = itemView.findViewById(R.id.textView_title);
            details = itemView.findViewById(R.id.textView_details);
            travellers = itemView.findViewById(R.id.button_travellers);
            expenses = itemView.findViewById(R.id.button_expenses);
            btnAddTraveller = itemView.findViewById(R.id.button_add_traveller);
            btnAddExpense = itemView.findViewById(R.id.button_add_expense);

            View.OnClickListener clickListener = v -> {

                if(v.getId() == travellers.getId()){
                    Log.d(TAG, "onClick: travellers");
                    recylerViewClickListener.showTravellers(getAdapterPosition());
                } else if(v.getId() == expenses.getId()){
                    Log.d(TAG, "onClick: expenses");
                    recylerViewClickListener.showExpenses(getAdapterPosition());
                } else if(v.getId() == btnAddTraveller.getId()){
                    recylerViewClickListener.addTraveller(getAdapterPosition());
                } else {
                    recylerViewClickListener.addExpense(getAdapterPosition());
                }
            };

            travellers.setOnClickListener(clickListener);
            expenses.setOnClickListener(clickListener);
            btnAddTraveller.setOnClickListener(clickListener);
            btnAddExpense.setOnClickListener(clickListener);

        }
    }

    public void setRecyclerViewClickListener(RecylerViewClickListener listener){
        this.recylerViewClickListener = listener;

    }

}
