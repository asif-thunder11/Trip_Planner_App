package org.thundercorp.tripplanner.DataModels;

import java.util.ArrayList;

public class TripUpload {

    int id;
    String name;
    String start_date, end_date;
    int creator_id;
    int route_id;
    int trip_package_id;
    ArrayList<String> traveller_ids;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public void setRoute_id(int route_id) {
        this.route_id = route_id;
    }

    public void setTrip_package_id(int trip_package_id) {
        this.trip_package_id = trip_package_id;
    }

    public void setTraveller_ids(ArrayList<String> traveller_ids) { this.traveller_ids = traveller_ids; }
}
