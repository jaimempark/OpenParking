package com.example.openparking;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Rating;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PurchaseCompleteActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonViewParking;
    private Button buttonRateSeller;
    private Button buttonMainMenu;
    private Dialog myDialog;

    private DatabaseReference mDatabase;
    private DatabaseReference testRef;

    private ParkingSpace parkingSpace;
    private User owner;

    private Intent rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_complete);


        parkingSpace = new ParkingSpace();
        Intent intent = getIntent();
        parkingSpace = intent.getParcelableExtra("parkingSpace");


        buttonViewParking = (Button)findViewById(R.id.btn_viewParking);
        buttonViewParking.setOnClickListener(this);

        buttonRateSeller = (Button) findViewById(R.id.btn_rate);
        buttonRateSeller.setOnClickListener(this);

        buttonMainMenu = (Button) findViewById(R.id.btn_mainMenu);
        buttonMainMenu.setOnClickListener(this);

        myDialog = new Dialog(this);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        owner = new User();
        retrieveOwner();

        //update database to show parking space as taken
        parkingSpace.setReservedStatus(Boolean.TRUE);
        System.out.println("Parkingspace isReserved: " + parkingSpace.getReservedStatus());
        writeData();

        rate = new Intent(PurchaseCompleteActivity.this, RatingActivity.class);
        rate.putExtra("parkingSpace", parkingSpace);
    }



    private void retrieveOwner()
    {
        DatabaseReference ref = mDatabase.child("users").child(parkingSpace.getOwnerID());

        //Retrieve seller information using ps.getOwnerID()
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                owner = new User();
                owner = dataSnapshot.getValue(User.class);
                Log.d("TAG", "Read successful, Owner: " + owner.toString());
            }

            @Override
            public void onStart() {
                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure(DatabaseError databaseError) {
                Log.d("ONFAILURE", "Failed");
            }
        });
    }

    public void readData(DatabaseReference ref, final OnGetDataListener listener){
        System.out.println("Reached READDATA function");
        listener.onStart();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure(databaseError);
            }
        });
    }

    public void writeData()
    {
        // WRITING to database after creating a parking instance object
        testRef = mDatabase.child("ParkingSpaces").child(parkingSpace.getZipcode()).child(parkingSpace.getID());
        testRef.setValue(parkingSpace);
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Database write successful!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database write failed");
            }
        });
    }



    public void showPopup() {
        TextView txtclose;
        Button btnFollow;
        TextView txtSellerName;
        TextView txtAddress;
        TextView txtIsAvailable;
        TextView txtOpenClose;
        TextView txtCost;
        RatingBar mRatingBar;

        myDialog.setContentView(R.layout.custom_window2);

        mRatingBar = (RatingBar) myDialog.findViewById(R.id.ratingBar3);
        displayRating(mRatingBar);

        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });

        txtSellerName = (TextView) myDialog.findViewById(R.id.txtSellerName);
        txtSellerName.setText(owner.getName());

        txtAddress = (TextView) myDialog.findViewById(R.id.txtAddress);
        txtAddress.setText(parkingSpace.getAddress() + ", " + parkingSpace.getZipcode());

        txtIsAvailable = (TextView) myDialog.findViewById(R.id.txtIsAvailable);
        if(parkingSpace.getReservedStatus())
            txtIsAvailable.setText("Sold");
        else
            txtIsAvailable.setText("Available");

        txtOpenClose = (TextView) myDialog.findViewById(R.id.txtOpenClose);
        txtOpenClose.setText("From " + parkingSpace.getOpentime() + " to " + parkingSpace.getClosetime());

        txtCost = (TextView) myDialog.findViewById(R.id.txtCost);
        txtCost.setText("$" + parkingSpace.getCost() + "0");

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void displayRating(RatingBar mRatingBar)
    {
        double rating = owner.getContributorRating();
        int numRated = owner.getTimesContributorRated();
        if(numRated == 0)
        {
            Toast.makeText(this, "This seller has not been rated.", Toast.LENGTH_SHORT).show();
        }
        else if(rating >= 4.5)
        {
            mRatingBar.setNumStars(5);
        }
        else if(rating >= 3.5)
        {
            mRatingBar.setNumStars(4);
        }
        else if(rating >= 2.5)
        {
            mRatingBar.setNumStars(3);
        }
        else if(rating >= 1.5)
        {
            mRatingBar.setNumStars(2);
        }
        else if(rating >= 0.5)
        {
            mRatingBar.setNumStars(1);
        }
        else
        {
            mRatingBar.setNumStars(0);
        }
    }



    @Override
    public void onClick(View view)
    {
        if(view == buttonViewParking)
        {
            showPopup();
        }
        if(view == buttonRateSeller)
        {
            startActivity(rate);
        }
        if(view == buttonMainMenu)
        {
            Log.v("!","main menu button clicked" );
            finish();
        }
    }
}
