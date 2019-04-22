package com.example.openparking;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateParkingInstanceActivity extends AppCompatActivity {


    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference ref;
    private DatabaseReference testRef;

    private ParkingSpace parkingSpace;
    private ParkingInstance parkingInstance;
    private User seller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_parking_instance);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference();
        testRef = ref.child("ZipCodes").child("90815").child("-Lb0SP_Qw2HzEbnDVCxm");

        // READING from database and retrieving a parking space object
        testRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                parkingSpace = dataSnapshot.getValue(ParkingSpace.class);
                Log.d("TAG", "Database read successful! " + parkingSpace.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database read failed");
            }
        });



        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        parkingInstance = new ParkingInstance(mUser.getUid(), parkingSpace);


        // WRITING to database after creating a parking instance object
        ref.child("parkingInstances").push().setValue(parkingInstance);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG", "Database write successful! " + parkingInstance.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Database write failed");
            }
        });




        //READING from firebase by using the sellerID from parkingInstance to retrieve a user
        ref.child("users").child(parkingInstance.getSellerID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                seller = dataSnapshot.getValue(User.class);
                Log.d("TAG", "The seller is " + seller.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", "Reading seller from database FAILED...");
            }
        });




        //DISPLAY NEWLY CREATED PARKING INSTANCE IN THE UI
    }
}
