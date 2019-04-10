package com.example.openparking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import android.location.Address;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.content.Intent;

public class SearchActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    /*
        A list view of ParkingInstances
        Categories: Price, Distance, Rating, New, ... (time of availability)
        Should display 6-8 instances at a time with a scrolling view
        Tile data per Instance:
            Primary image
            Location
            Availability
            Price
            Distance from user
            Rating
            Preferred Payment Method (icon)

        SHOULD BE SORTABLE BY:
            Distance from user
            Availability
            Price
            Rating
            Preferred Payment Method (icon)

        When a tile is selected, should call ViewParkingInstance activity like how we should do for map view

        How will we get ParkingInstance data from Firebase and show a sorted list to the user?
            (a) Keep multiple versions of list already sorted in Firebase, can just call it
            (b) Get data from Firebase and sort and show to user (can new data conflict with this?)
     */

    ArrayList<ParkingInstance> pList = new ArrayList<ParkingInstance>(); //data from firebase, most likely unsorted
    ArrayList<ParkingInstance> vList = new ArrayList<ParkingInstance>(); //data we've shown to the user
    int tilesToDisplay = 6; //6-8?
    int currentIndex = 1;

    //scrollable text view? redraw when needed?
    int loadResults(int currentIndex) {

            /*When the user presses the arrow buttons, load tile data.
              Do we need a seperate function for forward and backward arrows? (back means we can load what we had before?)) how about sorting?
            * */

        return currentIndex++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.TODO);

        //Use this template to get data from Firebase about classes we want to use
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        FirebaseUser fuser = firebaseAuth.getCurrentUser();
        final User user = new User();
        user.setId(fuser.getUid());

    }
    //SCRAP real time scrolling tiles!, lets do moderate scrolling list with arrows.

    //more info should load ViewParkingInstanceActivity
    //get current location, to sort instanced by closest
    //maybe option to show in map view??
    //need to constant refresh with new instances...
}
