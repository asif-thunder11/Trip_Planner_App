package org.thundercorp.tripplanner.DataModels;



import java.util.ArrayList;

public class TripItem {



    int trip_id;
    String title;
    String creator;
    String from;
    String to;
    String trip_package;
    String duration;
    String start_date, end_date;

    ArrayList<String> travellers;
    ArrayList<ExpenseItem> expenses;

    public ArrayList<String> getTravellers() {
        return travellers;
    }

    public ArrayList<ExpenseItem> getExpenses() {
        return expenses;
    }



    TripItem(){
        travellers = new ArrayList<>();
        expenses = new ArrayList<>();
    }

    public int getTrip_id() {
        return trip_id;
    }
    public String getTitle() {
        return title;
    }

    public String getCreator() {
        return creator;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getTrip_package() {
        return trip_package;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getDuration() {
        return duration;
    }
}
